package me.liye.open.share.util;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author knight@momo.com
 */
public class Base58Util {

    // Base58字符表
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final char[] ALPHABET_ARRAY = ALPHABET.toCharArray();
    private static final int BASE = ALPHABET_ARRAY.length;

    // 編碼
    public static String encode(byte[] input) {
        // 轉換爲BigInteger處理
        BigInteger bi = new BigInteger(1, input);
        StringBuilder sb = new StringBuilder();

        // 進行進制轉換
        while (bi.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = bi.divideAndRemainder(BigInteger.valueOf(BASE));
            sb.append(ALPHABET_ARRAY[divmod[1].intValue()]);
            bi = divmod[0];
        }

        // 反轉結果
        for (byte b : input) {
            if (b == 0) {
                sb.append(ALPHABET_ARRAY[0]);
            } else {
                break;
            }
        }

        return sb.reverse().toString();
    }

    // 解碼
    public static byte[] decode(String input) {
        BigInteger bi = BigInteger.ZERO;

        for (char c : input.toCharArray()) {
            bi = bi.multiply(BigInteger.valueOf(BASE)).add(BigInteger.valueOf(ALPHABET.indexOf(c)));
        }

        byte[] bytes = bi.toByteArray();

        // 檢查是否需要去掉高位的0
        if (bytes.length > 1 && bytes[0] == 0) {
            bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
        }

        // 處理前導0
        int leadingZeros = 0;
        for (char c : input.toCharArray()) {
            if (c == ALPHABET_ARRAY[0]) {
                leadingZeros++;
            } else {
                break;
            }
        }

        byte[] decoded = new byte[leadingZeros + bytes.length];
        System.arraycopy(bytes, 0, decoded, leadingZeros, bytes.length);

        return decoded;
    }

    public static void main(String[] args) {
        String original = "Hello, Base58!";
        // 編碼
        String encoded = encode(original.getBytes());
        System.out.println("Encoded: " + encoded);

        // 解碼
        byte[] decodedBytes = decode(encoded);
        String decoded = new String(decodedBytes);
        System.out.println("Decoded: " + decoded);
    }
}
