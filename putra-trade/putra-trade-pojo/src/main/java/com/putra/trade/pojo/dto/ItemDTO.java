package com.putra.trade.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemDTO implements Serializable {
    private Long id; // 编辑时携带

    @NotBlank(message = "商品标题不能为空")
    private String title;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private BigDecimal originalPrice;
    private Integer conditionLevel;
    private String cover;
    private String description;
    private String campusArea;

    /** 图册（封面之外的补充图 URL 列表） */
    private List<String> images;
}
