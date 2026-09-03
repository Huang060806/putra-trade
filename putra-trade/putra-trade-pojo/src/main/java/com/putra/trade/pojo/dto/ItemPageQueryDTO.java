package com.putra.trade.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ItemPageQueryDTO implements Serializable {
    private int page = 1;
    private int pageSize = 10;

    private String keyword;     // 标题/描述模糊搜索
    private Long categoryId;
    private String campusArea;  // 交货地点标签
    private Integer status;     // 管理端按状态筛选；用户端固定 1
    private Long sellerId;      // "我的发布" 按卖家过滤

    /** 排序: latest(默认) / price_asc / price_desc / hot */
    private String sortBy;
}
