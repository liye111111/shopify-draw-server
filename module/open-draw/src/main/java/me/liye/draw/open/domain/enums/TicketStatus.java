package me.liye.draw.open.domain.enums;

/**
 *
 */
public enum TicketStatus {
    PENDING, WIN, LOSE,
    // 不满足抽奖门槛要求
    INELIGIBLE;
    // 可以强制重抽的状态
//    public static List<TicketStatus> FORCE_SET = List.of(PENDING, WIN, LOSE);

}
