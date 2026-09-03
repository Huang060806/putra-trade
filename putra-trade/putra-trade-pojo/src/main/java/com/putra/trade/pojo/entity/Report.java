package com.putra.trade.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report implements Serializable {
    private Long id;
    private Long reporterId;
    private Integer targetType;     // 1商品 2用户
    private Long targetId;
    private String reason;
    private String images;
    private Integer status;         // 0待处理 1已处理 2已驳回
    private String handleResult;
    private Long handlerId;
    private LocalDateTime handleTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
