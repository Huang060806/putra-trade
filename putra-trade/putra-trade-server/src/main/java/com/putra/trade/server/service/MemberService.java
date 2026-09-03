package com.putra.trade.server.service;

import com.putra.trade.pojo.dto.LoginDTO;
import com.putra.trade.pojo.dto.MemberUpdateDTO;
import com.putra.trade.pojo.dto.RegisterDTO;
import com.putra.trade.pojo.vo.MemberLoginVO;

public interface MemberService {

    /**
     * 学生登录（学号或邮箱 + 密码），返回 Token
     */
    MemberLoginVO login(LoginDTO loginDTO);

    /**
     * 学生注册
     */
    void register(RegisterDTO registerDTO);

    /**
     * 修改个人资料（当前登录用户）
     */
    void updateProfile(MemberUpdateDTO updateDTO);
}
