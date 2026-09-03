package com.putra.trade.server.mapper;

import com.putra.trade.pojo.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT * FROM category ORDER BY sort ASC")
    List<Category> list();

    @Insert("INSERT INTO category (name, sort, create_time, update_time, create_user, update_user) " +
            "VALUES (#{name}, #{sort}, NOW(), NOW(), #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Category category);

    @Update("UPDATE category SET name = #{name}, sort = #{sort}, update_time = NOW(), update_user = #{updateUser} WHERE id = #{id}")
    void update(Category category);

    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteById(Long id);

    @Select("SELECT COUNT(*) FROM item WHERE category_id = #{id}")
    Integer countItemByCategoryId(Long id);
}
