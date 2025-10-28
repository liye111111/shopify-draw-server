package me.liye.open.share.util;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by liye on 2025-08-26.
 */
public class UUIDUtil {
    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base58Util.encode(bb.array());
    }

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateShortTicketSn(String prefix) {
        UUID uuid = UUID.randomUUID();
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        return prefix + toBase62(most) + toBase62(least);
    }

    private static String toBase62(long value) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(BASE62.charAt((int) (value & 61)));
            value >>>= 6;
        } while (value != 0);
        return sb.toString();
    }
    public static void main(String[] args) {
        System.out.println(shortUUID());
    }
}
