package com.putra.trade.server.mapper;

import com.github.pagehelper.Page;
import com.putra.trade.pojo.dto.ItemPageQueryDTO;
import com.putra.trade.pojo.entity.Item;
import com.putra.trade.pojo.vo.ItemDetailVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ItemMapper {

    @Insert("INSERT INTO item (title, category_id, seller_id, price, original_price, condition_level, " +
            "cover, description, campus_area, status, audit_remark, want_count, view_count, " +
            "create_time, update_time, create_user, update_user) " +
            "VALUES (#{title}, #{categoryId}, #{sellerId}, #{price}, #{originalPrice}, #{conditionLevel}, " +
            "#{cover}, #{description}, #{campusArea}, #{status}, #{auditRemark}, 0, 0, " +
            "NOW(), NOW(), #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Item item);

    void update(Item item);

    @Select("SELECT * FROM item WHERE id = #{id}")
    Item getById(Long id);

    @Delete("DELETE FROM item WHERE id = #{id}")
    void deleteById(Long id);

    Page<ItemDetailVO> pageQuery(ItemPageQueryDTO query);

    @Select("SELECT i.*, m.nickname AS sellerNickname, m.avatar AS sellerAvatar, c.name AS categoryName " +
            "FROM item i LEFT JOIN member m ON i.seller_id = m.id LEFT JOIN category c ON i.category_id = c.id " +
            "WHERE i.id = #{id}")
    ItemDetailVO getDetailById(Long id);

    @Update("UPDATE item SET view_count = view_count + 1 WHERE id = #{id}")
    void incrViewCount(Long id);

    @Update("UPDATE item SET want_count = want_count + 1 WHERE id = #{id}")
    void incrWantCount(Long id);

    @Update("UPDATE item SET want_count = GREATEST(want_count - 1, 0) WHERE id = #{id}")
    void decrWantCount(Long id);

    @Update("UPDATE item SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
