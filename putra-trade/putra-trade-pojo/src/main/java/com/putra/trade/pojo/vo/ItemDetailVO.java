package com.putra.trade.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailVO implements Serializable {
    private Long id;
    private String title;
    private Long categoryId;
    private String categoryName;
    private Long sellerId;
    private String sellerNickname;
    private String sellerAvatar;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer conditionLevel;
    private String cover;
    private List<String> images;
    private String description;
    private String campusArea;
    private Integer status;
    private String auditRemark;
    private Integer wantCount;
    private Integer viewCount;
    /** 当前登录用户是否已想要/已收藏 */
    private Boolean wanted;
    private Boolean favorited;
    private LocalDateTime createTime;
}
