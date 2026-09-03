package com.putra.trade.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 点"我想要"的响应：解锁卖家联系方式
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WantAddVO implements Serializable {
    private String sellerNickname;
    private String wechat;
    private String whatsapp;
    private String phone;
}
