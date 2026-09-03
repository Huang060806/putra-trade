package com.putra.trade.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生用户实体，对应表 member
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

    private Long id;
    private String studentNo;      // 学号（唯一）
    private String email;          // UPM 邮箱（唯一）
    private String password;       // BCrypt 哈希
    private String nickname;
    private String avatar;
    private Integer gender;        // 0未知 1男 2女
    private String phone;
    private String wechat;         // 微信号（点"我想要"后向买家展示）
    private String whatsapp;
    private String dormArea;       // 宿舍区域（KMR / College 10 / DKP...）
    private Integer banPublish;    // 1禁止发布 0正常
    private Integer banChat;       // 1禁止私聊 0正常
    private Integer status;        // 1正常 0封禁
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
