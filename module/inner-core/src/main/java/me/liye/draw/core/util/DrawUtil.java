package me.liye.draw.core.util;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public class DrawUtil {
    @Builder
    @Data
    public static class DrawResult {
        boolean win;
        double amount;
        String randomSeed;
    }

    /**
     * 抽奖算法
     * @param tickets 彩票，带有订单金额
     * @param maxWinners 中奖数量上限
     * @param totalAmount 奖池金额
     * @param rewardRandomFactor 奖金浮动随机因子, (0,1)区间，默认0.1, 等于0表示完全按权重分配， 等于1是两倍波动率
     * @return 中奖结果
     */
    public static Map<String, Double> draw(
            Map<String, Double> tickets, // 奖票号码-> 订单金额
            int maxWinners,// 中奖数量上限
            double totalAmount, // 奖池金额
            double rewardRandomFactor // 奖金浮动随机因子
    ) {
        // Step 1: 按权重抽取中奖用户
        List<String> winners = getWinners(tickets, maxWinners);
        // Step 2: 计算基础金额（按权重分配）
        double winnerTotalWeight = winners.stream()
                .mapToDouble(tickets::get).sum();

        // Step 3: 计算按权重分配的基础奖金
        Map<String, Double> baseAmount = new HashMap<>();
        for (String w : winners) {
            double amount = totalAmount * tickets.get(w) / winnerTotalWeight;
            baseAmount.put(w, amount);
        }

        // Step 3: 浮动金额（±weightDiffRate）
        Map<String, Double> floatAmount = new HashMap<>();
        Random r = new Random();
        for (String w : winners) {
            double factor = 1 + (r.nextDouble() * 2 - 1) * rewardRandomFactor; // [-rate, +rate]
            floatAmount.put(w, baseAmount.get(w) * factor);
        }

        // Step 4: 归一化金额（确保总额 = totalAmount）
        double floatSum = floatAmount.values().stream().mapToDouble(Double::doubleValue).sum();
        double normFactor = totalAmount / floatSum;

        Map<String, Double> finalAmount = new HashMap<>();
        for (String w : winners) {
            finalAmount.put(w, floatAmount.get(w) * normFactor);
        }

        return finalAmount;
    }


    /**
     * 抽取中奖彩票
     *
     * @param weights    带有权重的彩票
     * @param maxWinners 最大中奖数量
     * @return 中奖彩票
     */
    private static List<String> getWinners(Map<String, Double> weights, int maxWinners) {
        List<String> winners = new ArrayList<>();
        Random r = new Random();
        // 按权重降序排序
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(weights.entrySet());
        sortedEntries.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        Map<String, Double> weightCopy = sortedEntries.stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // 保持排序
                ));

        for (int i = 0; i < maxWinners && !weightCopy.isEmpty(); i++) {

            double total = weightCopy.values().stream().mapToDouble(Double::doubleValue).sum();
            double rand = r.nextDouble() * total;

            double cumulative = 0;
            String selected = null;

            for (String key : weightCopy.keySet()) {
                cumulative += weightCopy.get(key);
                if (rand <= cumulative) {
                    selected = key;
                    break;
                }
            }

            winners.add(selected);
            weightCopy.remove(selected); // 防止重复中奖
        }

        return winners;
    }

//
//    /**
//     * 确定性抽奖算法：
//     * - 相同 sn 结果固定
//     * - 中奖金额随机
//     *
//     * @param sn        唯一编号
//     * @param maxAmount 奖金上限（例如 100 表示最多100元）
//     */
//    public static DrawResult draw(String sn, double winRate, double maxAmount) {
//        double base = deterministicRandom(sn);
//        boolean win = base < winRate;
//        double amount = 0;
//
//        if (win) {
//            // 再生成一个确定性的金额随机值（避免与中奖概率相同）
//            double amountFactor = deterministicRandom(sn + ":amount");
//            amount = Math.round(amountFactor * maxAmount * 100.0) / 100.0; // 保留两位小数
//            if (amount < 0.01) amount = 0.01; // 避免中奖金额为0
//        }
//        log.info("sn={} => hash={}, win={}, amount={}", sn, String.format("%.4f", base), win, String.format("%.2f", amount));
//        return DrawResult.builder()
//                .win(win)
//                .amount(amount)
//                .randomSeed(String.valueOf(base))
//                .build();
//    }
//
//    /**
//     * 将字符串映射到 [0,1) 区间内的确定性 double 值
//     */
//    private static double deterministicRandom(String input) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
//            long hash = 0;
//            for (int i = 0; i < 8; i++) {
//                hash = (hash << 8) | (digest[i] & 0xFF);
//            }
//            return (hash >>> 1) / (double) (Long.MAX_VALUE);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
