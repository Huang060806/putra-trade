package com.putra.trade.server.mapper;

import com.github.pagehelper.Page;
import com.putra.trade.pojo.entity.ChatMessage;
import com.putra.trade.pojo.vo.ChatSessionVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMapper {

    @Insert("INSERT INTO chat_message (sender_id, receiver_id, item_id, content, is_read, create_time) " +
            "VALUES (#{senderId}, #{receiverId}, #{itemId}, #{content}, 0, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ChatMessage message);

    /** 与某人的聊天记录（倒序分页） */
    @Select("SELECT * FROM chat_message " +
            "WHERE (sender_id = #{memberId} AND receiver_id = #{peerId}) " +
            "   OR (sender_id = #{peerId} AND receiver_id = #{memberId}) " +
            "ORDER BY create_time DESC")
    Page<ChatMessage> pageHistory(@Param("memberId") Long memberId, @Param("peerId") Long peerId);

    /** 会话列表：每个对话对象取最新一条 + 未读数 */
    @Select("SELECT d.peerId, m.nickname AS peerNickname, m.avatar AS peerAvatar, " +
            "d.lastMessage, d.lastTime, " +
            "(SELECT COUNT(*) FROM chat_message c WHERE c.sender_id = d.peerId AND c.receiver_id = #{memberId} AND c.is_read = 0) AS unreadCount " +
            "FROM (SELECT (CASE WHEN t.sender_id = #{memberId} THEN t.receiver_id ELSE t.sender_id END) AS peerId, " +
            "      t.content AS lastMessage, t.create_time AS lastTime " +
            "      FROM chat_message t " +
            "      JOIN (SELECT MAX(id) AS max_id FROM chat_message " +
            "            WHERE sender_id = #{memberId} OR receiver_id = #{memberId} " +
            "            GROUP BY LEAST(sender_id, receiver_id), GREATEST(sender_id, receiver_id)) latest " +
            "      ON t.id = latest.max_id) d " +
            "JOIN member m ON m.id = d.peerId " +
            "ORDER BY d.lastTime DESC")
    List<ChatSessionVO> listSessions(Long memberId);

    @Update("UPDATE chat_message SET is_read = 1 WHERE sender_id = #{peerId} AND receiver_id = #{memberId} AND is_read = 0")
    void readAllFrom(@Param("memberId") Long memberId, @Param("peerId") Long peerId);

    @Select("SELECT COUNT(*) FROM chat_message WHERE receiver_id = #{memberId} AND is_read = 0")
    int countUnread(Long memberId);
}
