package com.putra.trade.server.service;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.dto.OrdersSubmitDTO;
import com.putra.trade.pojo.vo.OrderVO;

public interface OrderService {

    /** 下单：商品 在售→交易中（事务），通知卖家 */
    OrderVO submit(OrdersSubmitDTO dto);

    /** 模拟支付：待支付→待面交 */
    void pay(Long orderId);

    /** 买家确认面交完成：订单完成，商品→已售出 */
    void confirm(Long orderId);

    /** 取消订单（买家/卖家）：释放商品回在售 */
    void cancel(Long orderId, String reason);

    /** 我的订单（买/卖双向） */
    PageResult pageMine(int page, int pageSize);
}
