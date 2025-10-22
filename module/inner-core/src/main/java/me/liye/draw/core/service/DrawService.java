package me.liye.draw.core.service;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.DrawMapper;
import me.liye.draw.core.dao.TicketMapper;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.Draw;
import me.liye.draw.open.domain.Ticket;
import me.liye.draw.open.domain.TraceLog;
import me.liye.draw.open.domain.param.ApplyRewardParam;
import me.liye.draw.open.domain.param.CreateDrawParam;
import me.liye.draw.open.domain.param.ListDrawParam;
import me.liye.draw.open.domain.param.ListTicketParam;
import me.liye.open.share.util.ExceptionUtils;
import me.liye.open.share.util.TypeConvertor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DrawService {
    final ActivityService activityService;
    final DrawMapper drawMapper;
    final TicketMapper ticketMapper;
    final TraceLogService traceLogService;

    @Value("${applyRewardApi:http://localhost:8080/mock/applyReward}")
    String applyRewardApi;

    final static ExecutorService executorService = new ThreadPoolExecutor(
            4,
            16,
            60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(256),
            new NamedThreadFactory("DrawService-", false),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public Draw create(CreateDrawParam param) {
        Draw row = TypeConvertor.convert(param, Draw.class);
        drawMapper.insert(row);
        executorService.execute(
                new DrawTask(row.getId())
        );
        return drawMapper.selectById(row);
    }

    public List<Draw> list(ListDrawParam param) {
        return drawMapper.list(param);
    }


    class DrawTask implements Runnable {
        Long id;

        public DrawTask(Long id) {
            this.id = id;
        }

        @Override
        public void run() {
            log.info("start draw,id: {}", id);
            Draw draw = drawMapper.selectById(Draw.builder().id(id).build());
            Activity activity = activityService.activityMapper.selectById(
                    Activity.builder()
                            .id(draw.getActivityId())
                            .build());
            List<Ticket> tickets = ticketMapper.list(
                    ListTicketParam.builder()
                            .activityId(draw.getActivityId())
                            .build()
            );
            draw(tickets, activity);
            // call web3 contract api
        }

        private void draw(List<Ticket> tickets, Activity activity) {
            AtomicLong winCount = new AtomicLong(tickets.stream().filter(it -> it.getStatus().equals(Ticket.TicketStatus.WIN.name())).count());
            for (Ticket ticket : tickets) {
                Ticket.TicketStatus status = Ticket.TicketStatus.valueOf(ticket.getStatus());
                if (status != Ticket.TicketStatus.PENDING) {
                    log.info("ticket: {} is not PENDING, skip it", ticket.getId());
                    continue;
                }

//                if (winCount.intValue() > rule.getRewardAmount()) {
//                    ticket.setStatus(Ticket.TicketStatus.LOSE.name());
//                }
//
                double r = RandomUtils.nextDouble(0, 1);
//                if (r <= rule.getWinningProbability()) {
//                    ticket.setStatus(Ticket.TicketStatus.WIN.name());
//                    winCount.incrementAndGet();
//                } else {
//                    ticket.setStatus(Ticket.TicketStatus.LOSE.name());
//                }
                // write to db
                ticketMapper.updateStatusAndRandomSeed(ticket.getId(), ticket.getStatus(), String.valueOf(r));
                //
                callWeb3Api(ticket);
            }
        }

        private void callWeb3Api(Ticket ticket) {
            // call web3 contract api
            ApplyRewardParam param = ApplyRewardParam.builder()
                    .shopDomain(ticket.getShopDomain())
                    .activityId(ticket.getActivityId())
                    .rewardWalletAddress(ticket.getWalletAddress())
                    .amount(ticket.getAmount())
                    .build();
            String body = "";
            Exception ex = null;
            try (HttpResponse resp = HttpUtil.createPost(applyRewardApi)
                    .body(JSON.toJSONString(param), "application/json")
                    .execute()) {
                body = resp.body();
                log.info("call web3 api, status: {}, body: {}", resp.getStatus(), body);
                String txId = body;
                ticket.setTxId(txId);
                ticketMapper.updateTxId(ticket.getId(), txId);
            } catch (Exception e) {
                ex = e;
                log.error("call web3 api error", e);
            } finally {
                List<String> stack = ExceptionUtils.getRootCauseStackTrace(ex, 10, "me.liye.");
                traceLogService.create(TraceLog.builder()
                        .shopDomain(ticket.getShopDomain())
                        .name("callWeb3Api")
                        .data(Map.of(
                                        "url", applyRewardApi,
                                        "requestBody", param,
                                        "responseBody", body,
                                        "error", stack
                                )
                        )
                        .build());
            }
        }
    }
}
