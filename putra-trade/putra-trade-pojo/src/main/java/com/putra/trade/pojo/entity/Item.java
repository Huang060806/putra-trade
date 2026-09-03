package com.putra.trade.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {
    private Long id;
    private String title;
    private Long categoryId;
    private Long sellerId;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer conditionLevel; // 1全新未拆 2几乎全新 3轻微使用痕迹 4明显使用痕迹
    private String cover;
    private String description;
    private String campusArea;      // 交货地点标签
    private Integer status;         // 1在售 2预订/交易中 3已售出 0已下架 4待审核
    private String auditRemark;     // 审核备注
    private Integer wantCount;
    private Integer viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}
