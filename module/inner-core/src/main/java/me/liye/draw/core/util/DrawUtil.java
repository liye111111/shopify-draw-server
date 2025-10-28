package me.liye.draw.core.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
@Slf4j
public class DrawUtil {
    @Builder
    @Data
    public static class DrawResult {
        public final boolean win;
        public final double amount;

        public DrawResult(boolean win, double amount) {
            this.win = win;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "DrawResult{" +
                    "win=" + win +
                    ", amount=" + amount +
                    '}';
        }
    }

    /**
     * 确定性抽奖算法：
     * - 相同 sn 结果固定
     * - 中奖金额随机但确定
     *
     * @param sn        唯一编号
     * @param random    外部随机数（不参与逻辑）
     * @param maxAmount 奖金上限（例如 100 表示最多100元）
     */
    public static DrawResult draw(String sn, double winRate,double random, double maxAmount) {
        double base = deterministicRandom(sn);
        boolean win = base < winRate;
        double amount = 0;

        if (win) {
            // 再生成一个确定性的金额随机值（避免与中奖概率相同）
            double amountFactor = deterministicRandom(sn + ":amount");
            amount = Math.round(amountFactor * maxAmount * 100.0) / 100.0; // 保留两位小数
            if (amount < 0.01) amount = 0.01; // 避免中奖金额为0
        }

        System.out.printf("sn=%s => hash=%.4f, win=%s, amount=%.2f%n", sn, base, win, amount);
        return new DrawResult(win, amount);
    }

    /**
     * 将字符串映射到 [0,1) 区间内的确定性 double 值
     */
    private static double deterministicRandom(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            long hash = 0;
            for (int i = 0; i < 8; i++) {
                hash = (hash << 8) | (digest[i] & 0xFF);
            }
            return (hash >>> 1) / (double) (Long.MAX_VALUE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
