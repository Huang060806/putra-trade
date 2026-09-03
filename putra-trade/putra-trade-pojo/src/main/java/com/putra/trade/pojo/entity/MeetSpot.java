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
public class MeetSpot implements Serializable {
    private Long id;
    private Long memberId;
    private String contactName;
    private String contactPhone;
    private String spotName;
    private String remark;
    private Integer isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
