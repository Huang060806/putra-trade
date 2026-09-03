package com.putra.trade.server.mapper;

import com.github.pagehelper.Page;
import com.putra.trade.pojo.vo.ItemDetailVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FavoriteMapper {

    @Insert("INSERT IGNORE INTO favorite (member_id, item_id, create_time) VALUES (#{memberId}, #{itemId}, NOW())")
    int insert(Long memberId, Long itemId);

    @Delete("DELETE FROM favorite WHERE member_id = #{memberId} AND item_id = #{itemId}")
    void delete(Long memberId, Long itemId);

    @Select("SELECT COUNT(*) FROM favorite WHERE member_id = #{memberId} AND item_id = #{itemId}")
    int count(Long memberId, Long itemId);

    @Select("SELECT i.*, m.nickname AS sellerNickname, m.avatar AS sellerAvatar, c.name AS categoryName " +
            "FROM favorite f JOIN item i ON f.item_id = i.id " +
            "LEFT JOIN member m ON i.seller_id = m.id LEFT JOIN category c ON i.category_id = c.id " +
            "WHERE f.member_id = #{memberId} ORDER BY f.create_time DESC")
    Page<ItemDetailVO> pageByMember(Long memberId);
}
