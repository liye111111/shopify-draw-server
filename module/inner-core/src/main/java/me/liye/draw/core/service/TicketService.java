package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.TicketMapper;
import me.liye.draw.open.domain.Buyer;
import me.liye.draw.open.domain.Ticket;
import me.liye.draw.open.domain.param.CreateTicketParam;
import me.liye.draw.open.domain.param.ListBuyerParam;
import me.liye.draw.open.domain.param.ListTicketParam;
import me.liye.open.share.util.TypeConvertor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liye on 2025-09-24.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {
    final TicketMapper ticketMapper;
    final BuyerService buyerService;

    public Ticket create(CreateTicketParam param) {
        Ticket ticket = TypeConvertor.convert(param, Ticket.class);
        ticket.setStatus(Ticket.TicketStatus.PENDING.name());
        ticketMapper.insert(ticket);
        return ticketMapper.selectById(ticket);
    }

    public List<Ticket> list(ListTicketParam param) {
        List<Ticket> rows = ticketMapper.list(param);

        Map<String, List<Ticket>> shopTickets = rows.stream()
                .collect(Collectors.groupingBy(Ticket::getShopId));
        // 每个店铺单独处理，回填walletAddress
        for (String shopId : shopTickets.keySet()) {
            List<Ticket> tickets = shopTickets.get(shopId);
            List<String> emails = tickets.stream().map(Ticket::getEmail).toList();


            Map<String, String> emailWalletAddressMap = buyerService.list(ListBuyerParam.builder()
                            .pageSize(Integer.MAX_VALUE) // 注意要全部查询
                            .shopId(shopId)
                            .emails(emails)
                            .build())
                    .stream().collect(Collectors.toMap(Buyer::getEmail, Buyer::getWalletAddress));

            for (Ticket row : tickets) {
                row.setWalletAddress(emailWalletAddressMap.get(row.getEmail()));
            }
        }

        return rows;
    }
}
