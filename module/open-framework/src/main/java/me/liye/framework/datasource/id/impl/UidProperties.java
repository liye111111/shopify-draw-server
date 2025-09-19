package me.liye.framework.datasource.id.impl;

import me.liye.framework.datasource.id.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * UID 的配置
 *
 * @author knight@momo.com
 * @date 2019.02.20 10:31
 */
@ConfigurationProperties(prefix = "uid")
public class UidProperties {

    /**
     * 時間增量值佔用位數。當前時間相對於時間基點的增量值，單位爲秒
     */
    private int timeBits = 30;

    /**
     * 工作機器ID佔用的位數
     */
    private int workerBits = 16;

    /**
     * 序列號佔用的位數
     */
    private int seqBits = 7;

    /**
     * 時間基點. 例如 2019-02-20 (毫秒: 1550592000000)
     */
    private String epochStr = "2019-02-20";

    /**
     * 時間基點對應的毫秒數
     */
    private long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1550592000000L);

    /**
     * 是否容忍時鐘回撥, 默認:true
     */
    private boolean enableBackward = true;

    /**
     * 時鐘回撥最長容忍時間（秒）
     */
    private long maxBackwardSeconds = 1L;

    public int getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(int timeBits) {
        if (timeBits > 0) {
            this.timeBits = timeBits;
        }
    }

    public int getWorkerBits() {
        return workerBits;
    }

    public void setWorkerBits(int workerBits) {
        if (workerBits > 0) {
            this.workerBits = workerBits;
        }
    }

    public int getSeqBits() {
        return seqBits;
    }

    public void setSeqBits(int seqBits) {
        if (seqBits > 0) {
            this.seqBits = seqBits;
        }
    }

    public String getEpochStr() {
        return epochStr;
    }

    public void setEpochStr(String epochStr) {
        if (StringUtils.isNotBlank(epochStr)) {
            this.epochStr = epochStr;
            this.epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtils.parseByDayPattern(epochStr).getTime());
        }
    }

    public long getEpochSeconds() {
        return epochSeconds;
    }

    public boolean isEnableBackward() {
        return enableBackward;
    }

    public void setEnableBackward(boolean enableBackward) {
        this.enableBackward = enableBackward;
    }

    public long getMaxBackwardSeconds() {
        return maxBackwardSeconds;
    }

    public void setMaxBackwardSeconds(long maxBackwardSeconds) {
        this.maxBackwardSeconds = maxBackwardSeconds;
    }
}
