package com.putra.trade.server.mapper;

import com.github.pagehelper.Page;
import com.putra.trade.pojo.entity.Orders;
import com.putra.trade.pojo.vo.OrderVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (order_no, buyer_id, seller_id, item_id, item_title, price, meet_spot_info, " +
            "status, pay_method, order_time, create_time, update_time) " +
            "VALUES (#{orderNo}, #{buyerId}, #{sellerId}, #{itemId}, #{itemTitle}, #{price}, #{meetSpotInfo}, " +
            "#{status}, #{payMethod}, #{orderTime}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders orders);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders getById(Long id);

    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Orders getByOrderNo(String orderNo);

    void update(Orders orders);

    @Select("SELECT o.*, b.nickname AS buyerNickname, s.nickname AS sellerNickname, i.cover AS itemCover " +
            "FROM orders o LEFT JOIN member b ON o.buyer_id = b.id LEFT JOIN member s ON o.seller_id = s.id " +
            "LEFT JOIN item i ON o.item_id = i.id " +
            "WHERE o.buyer_id = #{memberId} OR o.seller_id = #{memberId} ORDER BY o.order_time DESC")
    Page<OrderVO> pageByMember(Long memberId);

    /** 超时的待支付订单 */
    @Select("SELECT * FROM orders WHERE status = 0 AND order_time < #{deadline}")
    List<Orders> getTimeoutUnpaid(LocalDateTime deadline);
}
