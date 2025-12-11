package me.liye.draw.open.domain.enums;


/**
 * 活动状态
 * < a href="https://sjpf5qn1680k.jp.larksuite.com/docx/NJo6dfevAoPoOyx1QhNjbehJpAh">PRD</a</>
 * <pre>
 * AWAITING_FUNDING > FUNDED_READY :  web3 -> web2 (web2提供api）
 * FUNDED_READY > PUBLISHED:  用户点击 “活动上线”
 * PUBLISHED > CLOSED:   活动到期
 * CLOSED >  CANCELLED: 目标未达成
 * CLOSED > DRAW_COMPLETED： 达到目标，进行抽奖计算，生成ticket中奖结果。 需要去掉平台手续费（默认值）
 * DRAW_COMPLETED > CLAIM_OPEN:  用户可以领奖，web3通知web2,  调用（新增）event api：（quest_id, event_name, params)。params中提供CLAIM_CLOSED时间
 * CLAIM_OPEN > CLAIM_CLOSED ：定时器根据web3提供的CLAIM_CLOSED时间（默认7天），修改状态
 * </pre>
 */
public enum ActivityStatus {
    /**
     * 暂存
     */
    DRAFT,

    /**
     * 已创建待注资
     */
    AWAITING_FUNDING,

    /**
     * 注资完成，参数锁定; 由web3发起event: merchantInit 触发
     */
    FUNDED_READY,

    /**
     * 对外可见，未到 T0
     */
    PUBLISHED,
    /**
     * 达标或到期冻结 GMV
     */
    CLOSED,

    /**
     * 抽奖计算中
     */
    DRAW_RUNNING,
    /**
     * 系统计算开奖
     */
    DRAW_COMPLETED,
    /**
     * 领奖开放; 由web3发起event: airdropOpen 触发
     */
    CLAIM_OPEN,
    /**
     * 领奖关闭
     */
    CLAIM_CLOSED,
    /**
     * 活动完结，数据保留
     */
    CANCELLED
}