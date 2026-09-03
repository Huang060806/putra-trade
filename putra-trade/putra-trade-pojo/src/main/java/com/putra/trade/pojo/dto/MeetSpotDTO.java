package com.putra.trade.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class MeetSpotDTO implements Serializable {
    private Long id;

    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    @NotBlank(message = "面交地点不能为空")
    private String spotName;

    private String remark;
    private Integer isDefault;
}
