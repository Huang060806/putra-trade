package com.putra.trade.server.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.dto.OrdersSubmitDTO;
import com.putra.trade.pojo.entity.Item;
import com.putra.trade.pojo.entity.MeetSpot;
import com.putra.trade.pojo.entity.Orders;
import com.putra.trade.pojo.vo.OrderVO;
import com.putra.trade.server.mapper.ItemMapper;
import com.putra.trade.server.mapper.MeetSpotMapper;
import com.putra.trade.server.mapper.OrderMapper;
import com.putra.trade.server.service.MessageService;
import com.putra.trade.server.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final int STATUS_UNPAID = 0;
    private static final int STATUS_TO_MEET = 1;
    private static final int STATUS_COMPLETED = 2;
    private static final int STATUS_CANCELLED = 3;

    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;
    private final MeetSpotMapper meetSpotMapper;
    private final MessageService messageService;

    @Override
    @Transactional
    public OrderVO submit(OrdersSubmitDTO dto) {
        Long buyerId = BaseContext.getCurrentId();

        Item item = itemMapper.getById(dto.getItemId());
        if (item == null || item.getStatus() != 1) {
            throw new BusinessException("商品不在在售状态，可能已被别人抢先下单");
        }
        if (item.getSellerId().equals(buyerId)) {
            throw new BusinessException("不能购买自己的商品");
        }

        MeetSpot spot = meetSpotMapper.getById(dto.getMeetSpotId());
        if (spot == null || !spot.getMemberId().equals(buyerId)) {
            throw new BusinessException("面交地点不存在");
        }

        String orderNo = "PT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));

        Orders orders = Orders.builder()
                .orderNo(orderNo)
                .buyerId(buyerId)
                .sellerId(item.getSellerId())
                .itemId(item.getId())
                .itemTitle(item.getTitle())
                .price(item.getPrice())
                .meetSpotInfo(spot.getSpotName() + " | " + spot.getContactName() + " " + spot.getContactPhone())
                .status(STATUS_UNPAID)
                .payMethod(1)
                .orderTime(LocalDateTime.now())
                .build();
        orderMapper.insert(orders);

        // 商品锁定：在售 → 交易中（取消/超时后释放）
        itemMapper.updateStatus(item.getId(), 2);

        messageService.send(item.getSellerId(), MessageService.TYPE_ORDER,
                "你的商品「" + item.getTitle() + "」被下单了，订单号 " + orderNo, orders.getId());

        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(orders, vo);
        return vo;
    }

    @Override
    public void pay(Long orderId) {
        Orders orders = checkBuyer(orderId);
        if (orders.getStatus() != STATUS_UNPAID) {
            throw new BusinessException("订单不在待支付状态");
        }
        // 模拟支付：直接置为已支付
        Orders update = Orders.builder().id(orderId).status(STATUS_TO_MEET).payTime(LocalDateTime.now()).build();
        orderMapper.update(update);
        messageService.send(orders.getSellerId(), MessageService.TYPE_ORDER_CHANGE,
                "订单 " + orders.getOrderNo() + " 买家已完成支付，请尽快联系买家约定面交。", orderId);
    }

    @Override
    @Transactional
    public void confirm(Long orderId) {
        Orders orders = checkBuyer(orderId);
        if (orders.getStatus() != STATUS_TO_MEET) {
            throw new BusinessException("订单不在待面交状态");
        }
        Orders update = Orders.builder().id(orderId).status(STATUS_COMPLETED).completeTime(LocalDateTime.now()).build();
        orderMapper.update(update);
        itemMapper.updateStatus(orders.getItemId(), 3);
        messageService.send(orders.getSellerId(), MessageService.TYPE_ORDER_CHANGE,
                "订单 " + orders.getOrderNo() + " 买家已确认收货，交易完成！", orderId);
    }

    @Override
    @Transactional
    public void cancel(Long orderId, String reason) {
        Orders orders = orderMapper.getById(orderId);
        if (orders == null) {
            throw new BusinessException("订单不存在");
        }
        Long currentId = BaseContext.getCurrentId();
        if (!orders.getBuyerId().equals(currentId) && !orders.getSellerId().equals(currentId)) {
            throw new BusinessException("只能取消自己的订单");
        }
        if (orders.getStatus() == STATUS_COMPLETED || orders.getStatus() == STATUS_CANCELLED) {
            throw new BusinessException("订单已结束，不能取消");
        }
        doCancel(orders, reason);

        Long otherSide = orders.getBuyerId().equals(currentId) ? orders.getSellerId() : orders.getBuyerId();
        messageService.send(otherSide, MessageService.TYPE_ORDER_CHANGE,
                "订单 " + orders.getOrderNo() + " 已被对方取消：" + reason, orderId);
    }

    @Override
    public PageResult pageMine(int page, int pageSize) {
        Page<OrderVO> p = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> orderMapper.pageByMember(BaseContext.getCurrentId()));
        return new PageResult(p.getTotal(), p.getResult());
    }

    /** 超时取消（Spring Task 调用）：释放商品 */
    @Transactional
    public void doCancel(Orders orders, String reason) {
        Orders update = Orders.builder()
                .id(orders.getId()).status(STATUS_CANCELLED)
                .cancelReason(reason).cancelTime(LocalDateTime.now()).build();
        orderMapper.update(update);
        itemMapper.updateStatus(orders.getItemId(), 1);
    }

    private Orders checkBuyer(Long orderId) {
        Orders orders = orderMapper.getById(orderId);
        if (orders == null) {
            throw new BusinessException("订单不存在");
        }
        if (!orders.getBuyerId().equals(BaseContext.getCurrentId())) {
            throw new BusinessException("只能操作自己作为买家的订单");
        }
        return orders;
    }
}
