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

    public static void main(String[] args) {
        System.out.println(shortUUID());
    }
}
