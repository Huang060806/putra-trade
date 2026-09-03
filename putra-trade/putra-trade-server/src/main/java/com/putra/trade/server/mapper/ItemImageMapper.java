package com.putra.trade.server.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemImageMapper {

    @Insert("INSERT INTO item_image (item_id, url, sort) VALUES (#{itemId}, #{url}, #{sort})")
    void insert(@Param("itemId") Long itemId, @Param("url") String url, @Param("sort") Integer sort);

    @Select("SELECT url FROM item_image WHERE item_id = #{itemId} ORDER BY sort ASC")
    List<String> getUrlsByItemId(Long itemId);

    @Delete("DELETE FROM item_image WHERE item_id = #{itemId}")
    void deleteByItemId(Long itemId);
}
