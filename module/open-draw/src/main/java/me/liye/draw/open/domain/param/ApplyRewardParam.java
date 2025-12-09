package me.liye.draw.open.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BaseDTO;

/**
 * Created by liye on 2025-09-24.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyRewardParam extends BaseDTO {
    /**
     * 活动ID
     */
    Long activityId;
    /**
     * 中奖用户的钱包地址
     */
    String rewardWalletAddress;
    /**
     * token数量
     */
    String amount;
}
