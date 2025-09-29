/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.liye.framework.datasource.id.impl;


import me.liye.framework.datasource.id.BitsAllocator;
import me.liye.framework.datasource.id.IDGenerator;
import me.liye.framework.datasource.id.buffer.BufferPaddingExecutor;
import me.liye.framework.datasource.id.buffer.RejectedPutBufferHandler;
import me.liye.framework.datasource.id.buffer.RejectedTakeBufferHandler;
import me.liye.framework.datasource.id.buffer.RingBuffer;
import me.liye.framework.datasource.id.exception.UidGenerateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cached implementation of {@link IDGenerator} extends
 * from {@link DefaultIDGenerator}, based on a lock free {@link RingBuffer}<p>
 * <p>
 * The spring properties you can specified as below:<br>
 * <li><b>boostPower:</b> RingBuffer size boost for a power of 2, Sample: boostPower is 3, it means the buffer size
 * will be <code>({@link BitsAllocator#getMaxSequence()} + 1) &lt;&lt;
 * {@link #boostPower}</code>, Default as {@value #DEFAULT_BOOST_POWER}
 * <li><b>paddingFactor:</b> Represents a percent value of (0 - 100). When the count of rest available UIDs reach the
 * threshold, it will trigger padding buffer. Default as{@link RingBuffer#DEFAULT_PADDING_PERCENT}
 * Sample: paddingFactor=20, bufferSize=1000 -> threshold=1000 * 20 /100, padding buffer will be triggered when tail-cursor<threshold
 * <li><b>scheduleInterval:</b> Padding buffer in a schedule, specify padding buffer interval, Unit as second
 * <li><b>rejectedPutBufferHandler:</b> Policy for rejected put buffer. Default as discard put request, just do logging
 * <li><b>rejectedTakeBufferHandler:</b> Policy for rejected take buffer. Default as throwing up an exception
 *
 * @author knight@momo.com
 * @author knight@momo.com
 */
@Component("cachedIDGenerator")
@ConfigurationProperties(prefix = "uid.cached-uid-generator")
public class CachedIDGenerator extends DefaultIDGenerator implements DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CachedIDGenerator.class);
    private static final int DEFAULT_BOOST_POWER = 3;

    // --------------------- 配置屬性 begin ---------------------
    /**
     * RingBuffer size擴容參數, 可提高UID生成的吞吐量.
     * 默認:3， 原bufferSize=8192, 擴容後bufferSize= 8192 << 3 = 65536
     */
    private int boostPower = DEFAULT_BOOST_POWER;
    /**
     * 指定何時向RingBuffer中填充UID, 取值爲百分比(0, 100), 默認爲50
     * 舉例: bufferSize=1024, paddingFactor=50 -> threshold=1024 * 50 / 100 = 512.
     * 當環上可用UID數量 < 512時, 將自動對RingBuffer進行填充補全
     */
    private int paddingFactor = RingBuffer.DEFAULT_PADDING_PERCENT;
    /**
     * 另外一種RingBuffer填充時機, 在Schedule線程中, 週期性檢查填充
     * 默認:不配置此項, 即不使用Schedule線程. 如需使用, 請指定Schedule線程時間間隔, 單位:秒
     */
    private Long scheduleInterval;
    // --------------------- 配置屬性 end -----------------------

    /** 拒絕策略: 當環已滿, 無法繼續填充時
     默認無需指定, 將丟棄Put操作, 僅日誌記錄. 如有特殊需求, 請實現RejectedPutBufferHandler接口(支持Lambda表達式)並以@Autowired方式注入 */
    @Autowired(required = false)
    private RejectedPutBufferHandler rejectedPutBufferHandler;

    /** 拒絕策略: 當環已空, 無法繼續獲取時
     默認無需指定, 將記錄日誌, 並拋出UidGenerateException異常. 如有特殊需求, 請實現RejectedTakeBufferHandler接口(支持Lambda表達式)並以@Autowired方式注入 */
    @Autowired(required = false)
    private RejectedTakeBufferHandler rejectedTakeBufferHandler;

    /**
     * RingBuffer
     */
    private RingBuffer ringBuffer;
    private BufferPaddingExecutor bufferPaddingExecutor;

    public CachedIDGenerator(UidProperties uidProperties) {
        super(uidProperties);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // initialize workerId & bitsAllocator
        super.afterPropertiesSet();

        // initialize RingBuffer & RingBufferPaddingExecutor
        this.initRingBuffer();
        LOGGER.info("Initialized RingBuffer successfully.");
    }

    @Override
    public long getUID() {
        try {
            return ringBuffer.take();
        } catch (Exception e) {
            LOGGER.error("Generate unique id exception. ", e);
            throw new UidGenerateException(e);
        }
    }

    @Override
    public String parseUID(long uid) {
        return super.parseUID(uid);
    }

    @Override
    public void destroy() throws Exception {
        bufferPaddingExecutor.shutdown();
    }

    /**
     * Get the UIDs in the same specified second under the max sequence
     *
     * @param currentSecond
     * @return UID list, size of {@link BitsAllocator#getMaxSequence()} + 1
     */
    protected List<Long> nextIdsForOneSecond(long currentSecond) {
        // Initialize result list size of (max sequence + 1)
        int listSize = (int) bitsAllocator.getMaxSequence() + 1;
        List<Long> uidList = new ArrayList<>(listSize);

        // Allocate the first sequence of the second, the others can be calculated with the offset
        long firstSeqUid = bitsAllocator.allocate(currentSecond - uidProperties.getEpochSeconds(), workerId, 0L);
        for (int offset = 0; offset < listSize; offset++) {
            uidList.add(firstSeqUid + offset);
        }

        return uidList;
    }

    /**
     * Initialize RingBuffer & RingBufferPaddingExecutor
     */
    private void initRingBuffer() {
        // initialize RingBuffer
        int bufferSize = ((int) bitsAllocator.getMaxSequence() + 1) << boostPower;
        this.ringBuffer = new RingBuffer(bufferSize, paddingFactor);
        LOGGER.info("Initialized ring buffer size:{}, paddingFactor:{}", bufferSize, paddingFactor);

        // initialize RingBufferPaddingExecutor
        boolean usingSchedule = (scheduleInterval != null);
        this.bufferPaddingExecutor = new BufferPaddingExecutor(ringBuffer, this::nextIdsForOneSecond, usingSchedule);
        if (usingSchedule) {
            bufferPaddingExecutor.setScheduleInterval(scheduleInterval);
        }

        LOGGER.info("Initialized BufferPaddingExecutor. Using schdule:{}, interval:{}", usingSchedule, scheduleInterval);

        // set rejected put/take handle policy
        this.ringBuffer.setBufferPaddingExecutor(bufferPaddingExecutor);
        if (rejectedPutBufferHandler != null) {
            this.ringBuffer.setRejectedPutHandler(rejectedPutBufferHandler);
        }
        if (rejectedTakeBufferHandler != null) {
            this.ringBuffer.setRejectedTakeHandler(rejectedTakeBufferHandler);
        }

        // fill in all slots of the RingBuffer
        bufferPaddingExecutor.paddingBuffer();

        // start buffer padding threads
        bufferPaddingExecutor.start();
    }

    /**
     * Setters for spring property
     */
    public void setBoostPower(int boostPower) {
        Assert.isTrue(boostPower > 0, "Boost power must be positive!");
        this.boostPower = boostPower;
    }

    public void setPaddingFactor(int paddingFactor) {
        Assert.isTrue(paddingFactor > 0 && paddingFactor < 100, "padding factor must be in (0, 100)!");
        this.paddingFactor = paddingFactor;
    }

    public void setRejectedPutBufferHandler(RejectedPutBufferHandler rejectedPutBufferHandler) {
        Assert.notNull(rejectedPutBufferHandler, "RejectedPutBufferHandler can't be null!");
        this.rejectedPutBufferHandler = rejectedPutBufferHandler;
    }

    public void setRejectedTakeBufferHandler(RejectedTakeBufferHandler rejectedTakeBufferHandler) {
        Assert.notNull(rejectedTakeBufferHandler, "RejectedTakeBufferHandler can't be null!");
        this.rejectedTakeBufferHandler = rejectedTakeBufferHandler;
    }

    public void setScheduleInterval(long scheduleInterval) {
        Assert.isTrue(scheduleInterval > 0, "Schedule interval must positive!");
        this.scheduleInterval = scheduleInterval;
    }

}
