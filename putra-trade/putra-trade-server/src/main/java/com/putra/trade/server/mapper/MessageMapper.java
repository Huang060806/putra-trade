package com.putra.trade.server.mapper;

import com.github.pagehelper.Page;
import com.putra.trade.pojo.entity.Message;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MessageMapper {

    @Insert("INSERT INTO message (receiver_id, type, content, order_id, is_read, create_time) " +
            "VALUES (#{receiverId}, #{type}, #{content}, #{orderId}, 0, NOW())")
    void insert(Message message);

    @Select("SELECT * FROM message WHERE receiver_id = #{memberId} ORDER BY create_time DESC")
    Page<Message> pageByReceiver(Long memberId);

    @Select("SELECT COUNT(*) FROM message WHERE receiver_id = #{memberId} AND is_read = 0")
    int countUnread(Long memberId);

    @Update("UPDATE message SET is_read = 1 WHERE receiver_id = #{memberId}")
    void readAll(Long memberId);
}
