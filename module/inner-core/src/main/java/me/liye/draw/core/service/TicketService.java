package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.TicketMapper;
import me.liye.draw.open.domain.Ticket;
import me.liye.draw.open.domain.param.CreateTicketParam;
import me.liye.draw.open.domain.param.ListTicketParam;
import me.liye.open.share.util.TypeConvertor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liye on 2025-09-24.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {
    final TicketMapper ticketMapper;
    public Ticket create(CreateTicketParam param) {
        Ticket ticket = TypeConvertor.convert(param, Ticket.class);
        ticket.setStatus(Ticket.TicketStatus.PENDING.name());
        ticketMapper.insert(ticket);
        return ticketMapper.selectById(ticket);
    }

    public List<Ticket> list(ListTicketParam param) {
        return ticketMapper.list(param);
    }
}
