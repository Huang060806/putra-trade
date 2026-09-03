package com.putra.trade.server.service;

import com.putra.trade.pojo.dto.LoginDTO;
import com.putra.trade.pojo.vo.EmployeeLoginVO;

public interface EmployeeService {

    /**
     * 管理员登录
     */
    EmployeeLoginVO login(LoginDTO loginDTO);
}
