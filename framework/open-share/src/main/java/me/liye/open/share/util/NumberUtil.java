package me.liye.open.share.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author knight@momo.com
 */
public class NumberUtil {
    public static Long parseLongIfPreset(String str) {
        if (StringUtils.isNotBlank(str)) {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
