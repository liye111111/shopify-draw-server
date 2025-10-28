package me.liye.draw.open.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.draw.open.domain.ActivityTarget;
import me.liye.draw.open.domain.DrawRule;
import me.liye.draw.open.domain.enums.ActivityStatus;
import me.liye.draw.open.domain.enums.DrawTriggerType;
import me.liye.open.share.rpc.BaseDTO;

import java.util.Date;

/**
 * Created by liye on 2025-09-23.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityParam extends BaseDTO {
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

}
