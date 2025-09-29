package me.liye.draw.core.dao;

import me.liye.draw.open.domain.Ticket;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by liye on 2025-09-19.
 */
@SpringBootTest
class TicketMapperTest {
    @Autowired
    TicketMapper ticketMapper;

    @Test
    void test(){
        Ticket row = Instancio.create(Ticket.class);
        ticketMapper.insert(row);
    }


}