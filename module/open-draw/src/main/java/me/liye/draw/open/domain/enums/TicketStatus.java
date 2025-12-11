package me.liye.draw.open.domain.enums;

/**
 *
 */
public enum TicketStatus {
    PENDING, WIN, LOSE,
    // GMV不满足抽奖目标
    SKIP_BY_GMV_TARGET,
    // 订单金额不满足抽奖门槛要求
    SKIP_BY_ORDER_PRICE,
    // 扣除服务费后奖池为0
    SKIP_BY_SERVICE_FEE,
    ;
    // 可以强制重抽的状态
//    public static List<TicketStatus> FORCE_SET = List.of(PENDING, WIN, LOSE);

}
