package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.draw.open.domain.enums.ActivityStatus;
import me.liye.draw.open.domain.enums.DrawTriggerType;
import me.liye.open.share.dataobject.BaseDataObjectWithJsonColumn;

import java.util.Date;

/**
 * 抽奖活动配置
 * Created by liye on 2025-09-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Activity extends BaseDataObjectWithJsonColumn {
    /**
     * 店铺
     */
    String shopDomain;
    /**
     * 活动名称
     */
    String name;
    /**
     * 活动描述
     */
    String description;

    /**
     * 活动开始时间
     */
    Date startTime;
    /**
     * 活动结束时间
     */
    Date endTime;


    /**
     * 抽奖触发方式
     */
    DrawTriggerType drawTriggerType;

    /**
     * 背景图片(1200x400)
     */
    String backgroundImage;

    /**
     * 预览图片(400*400)
     */
    String previewImage;

    /**
     * 活动目标
     */
    ActivityTarget activityTarget;

    /**
     * 用户总消费参与抽奖门槛
     */
    String minUserTotalSpend;

    /**
     * 单笔订单抽奖门槛
     */
    String minOrderSingleSpend;

    /**
     * 活动状态
     */
    ActivityStatus status;

    /**
     * 抽奖规则
     */

    DrawRule drawRule;


    /**
     * 钱包地址
     */
    String walletAddress;

    /**
     * 充值金额
     * dev: DCDpBz2wzXpX4rD1F7o9jfxnzGEJ4AsP4TgDaaVi6ude
     */
    String rewardToken;
}
