package com.putra.trade.server.mapper;

import com.github.pagehelper.Page;
import com.putra.trade.pojo.entity.Report;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ReportMapper {

    @Insert("INSERT INTO report (reporter_id, target_type, target_id, reason, images, status, create_time, update_time) " +
            "VALUES (#{reporterId}, #{targetType}, #{targetId}, #{reason}, #{images}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Report report);

    @Select("SELECT * FROM report WHERE id = #{id}")
    Report getById(Long id);

    Page<Report> pageQuery(@Param("targetType") Integer targetType, @Param("status") Integer status);

    @Update("UPDATE report SET status = #{status}, handle_result = #{handleResult}, handler_id = #{handlerId}, " +
            "handle_time = NOW(), update_time = NOW() WHERE id = #{id}")
    void handle(Report report);
}
