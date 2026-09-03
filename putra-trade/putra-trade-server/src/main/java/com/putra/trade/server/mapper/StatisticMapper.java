package com.putra.trade.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticMapper {

    @Select("SELECT COUNT(*) FROM member")
    Integer countMembers();

    @Select("SELECT COUNT(*) FROM item")
    Integer countItems();

    @Select("SELECT COUNT(*) FROM orders WHERE status = 2")
    Integer countCompletedOrders();

    @Select("SELECT COUNT(*) FROM report WHERE status = 0")
    Integer countPendingReports();

    @Select("SELECT COUNT(*) FROM item WHERE status = 4")
    Integer countPendingAudit();

    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS date, COUNT(*) AS count FROM member " +
            "WHERE create_time >= #{begin} GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') ORDER BY date")
    List<Map<String, Object>> countMembersByDate(LocalDate begin);

    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS date, COUNT(*) AS count FROM item " +
            "WHERE create_time >= #{begin} GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') ORDER BY date")
    List<Map<String, Object>> countItemsByDate(LocalDate begin);

    @Select("SELECT DATE_FORMAT(handle_time, '%Y-%m-%d') AS date, COUNT(*) AS count FROM report " +
            "WHERE status = 1 AND handle_time >= #{begin} GROUP BY DATE_FORMAT(handle_time, '%Y-%m-%d') ORDER BY date")
    List<Map<String, Object>> countHandledReportsByDate(LocalDate begin);

    @Select("SELECT c.name AS name, COUNT(i.id) AS count FROM category c LEFT JOIN item i ON i.category_id = c.id " +
            "GROUP BY c.id, c.name ORDER BY count DESC")
    List<Map<String, Object>> countItemsByCategory();

    @Select("SELECT title, want_count AS wantCount, view_count AS viewCount FROM item " +
            "WHERE status IN (1,2) ORDER BY want_count DESC LIMIT 10")
    List<Map<String, Object>> topItemsByWant();
}
