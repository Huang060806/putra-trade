package com.putra.trade.server.mapper;

import com.putra.trade.pojo.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 管理员登录：按用户名查询
     */
    @Select("SELECT * FROM employee WHERE username = #{username}")
    Employee getByUsername(@Param("username") String username);
}
