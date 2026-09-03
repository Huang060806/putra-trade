package com.putra.trade.server.mapper;

import com.github.pagehelper.Page;
import com.putra.trade.pojo.vo.ItemDetailVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WantMapper {

    @Insert("INSERT IGNORE INTO want (member_id, item_id, create_time) VALUES (#{memberId}, #{itemId}, NOW())")
    int insert(Long memberId, Long itemId);

    @Delete("DELETE FROM want WHERE member_id = #{memberId} AND item_id = #{itemId}")
    int delete(Long memberId, Long itemId);

    @Select("SELECT COUNT(*) FROM want WHERE member_id = #{memberId} AND item_id = #{itemId}")
    int count(Long memberId, Long itemId);

    /**
     * 我想要的列表：能看到商品实时状态 + 卖家最新联系方式（决策⑰）
     */
    @Select("SELECT i.*, m.nickname AS sellerNickname, m.avatar AS sellerAvatar, c.name AS categoryName " +
            "FROM want w JOIN item i ON w.item_id = i.id " +
            "LEFT JOIN member m ON i.seller_id = m.id LEFT JOIN category c ON i.category_id = c.id " +
            "WHERE w.member_id = #{memberId} ORDER BY w.create_time DESC")
    Page<ItemDetailVO> pageByMember(Long memberId);
}
