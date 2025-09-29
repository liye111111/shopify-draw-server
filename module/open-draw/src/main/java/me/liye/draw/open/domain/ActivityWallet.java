package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.dataobject.BaseDataObjectWithJsonColumn;

/**
 * 活动钱包
 * Created by liye on 2025-09-24.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityWallet extends BaseDataObjectWithJsonColumn {
    String shopDomain;
    String walletAddress;
}
