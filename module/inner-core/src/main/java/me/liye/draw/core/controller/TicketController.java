package me.liye.draw.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.service.TicketService;
import me.liye.draw.open.domain.Ticket;
import me.liye.draw.open.domain.param.CreateTicketParam;
import me.liye.draw.open.domain.param.ListTicketParam;
import me.liye.open.share.rpc.RpcResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static me.liye.open.share.rpc.RpcResult.success;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {
    final TicketService ticketService;

    @PostMapping("/create")
    public RpcResult<Ticket> create(@RequestBody CreateTicketParam param) {
        return success(ticketService.create(param));
    }

    @GetMapping("/list")
    public RpcResult<List<Ticket>> list(ListTicketParam param) {
        return success(ticketService.list(param));
    }
}
