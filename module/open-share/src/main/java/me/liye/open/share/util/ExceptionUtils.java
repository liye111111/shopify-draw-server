package me.liye.open.share.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author knight@momo.com
 */
public class ExceptionUtils {

    public static final int STACK_TOP_LINES = 3;

    public static List<String> getRootCauseStackTrace(Throwable throwable, String... prefix) {
        return getRootCauseStackTrace(throwable, STACK_TOP_LINES, prefix);
    }

    public static List<String> getRootCauseStackTrace(Throwable throwable, int topN, String... prefix) {
        if (prefix == null || prefix.length == 0) {
            prefix = new String[]{"*"};
        }


        List<String> prefixSet = Optional.ofNullable(prefix).map(it -> Arrays.stream(it).toList()).orElse(Lists.newArrayList());


        String[] exArr = Optional.ofNullable(throwable)
                .map(org.apache.commons.lang3.exception.ExceptionUtils::getRootCauseStackTrace)
                .orElse(ArrayUtils.EMPTY_STRING_ARRAY);
        if (exArr.length < topN) {
            return Arrays.stream(exArr).toList();
        }

        AtomicInteger lines = new AtomicInteger(0);

        return Arrays.stream(exArr).filter(it -> {
                    boolean keep = false;
                    for (String s : prefixSet) {
                        if (("*".equals(s) || it.contains(s)) && lines.incrementAndGet() <= topN) {
                            keep = true;
                            break;
                        }
                    }
                    return keep;
                }).map(String::trim)
                .collect(Collectors.toList());

    }
}
