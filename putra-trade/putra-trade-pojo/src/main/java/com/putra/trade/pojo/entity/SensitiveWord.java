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
public class SensitiveWord implements Serializable {
    private Long id;
    private String word;
    private Integer type;       // 1政治 2色情 3违禁品
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
}
