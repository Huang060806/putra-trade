package com.putra.trade.server.task;

import com.putra.trade.pojo.entity.Orders;
import com.putra.trade.server.mapper.OrderMapper;
import com.putra.trade.server.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单超时处理：待支付超过 30 分钟自动取消并释放商品
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTimeoutTask {

    private static final int PAY_TIMEOUT_MINUTES = 30;

    private final OrderMapper orderMapper;
    private final OrderServiceImpl orderService;

    @Scheduled(cron = "0 * * * * ?") // 每分钟
    public void processTimeoutOrders() {
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(PAY_TIMEOUT_MINUTES);
        List<Orders> timeoutOrders = orderMapper.getTimeoutUnpaid(deadline);
        for (Orders order : timeoutOrders) {
            log.info("订单超时自动取消: {}", order.getOrderNo());
            orderService.doCancel(order, "超时未支付，系统自动取消");
        }
    }
}
