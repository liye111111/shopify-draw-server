package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.draw.open.domain.enums.BuyerStatus;
import me.liye.open.share.dataobject.BaseDataObjectWithEmptyJsonColumn;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Buyer extends BaseDataObjectWithEmptyJsonColumn {
    String name;
    BuyerStatus status;
    String email;
    /**
     * 钱包地址
     */
    String walletAddress;
}
