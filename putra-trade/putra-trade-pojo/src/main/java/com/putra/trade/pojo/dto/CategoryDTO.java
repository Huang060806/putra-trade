package com.putra.trade.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private Integer sort;
}
