package com.putra.trade.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class ChatSendDTO implements Serializable {

    @NotNull(message = "接收人不能为空")
    private Long receiverId;

    private Long itemId;

    @NotBlank(message = "消息内容不能为空")
    @Size(max = 500, message = "消息最长 500 字")
    private String content;
}
