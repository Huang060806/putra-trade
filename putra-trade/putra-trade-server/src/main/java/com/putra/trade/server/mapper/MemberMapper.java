package com.putra.trade.server.mapper;

import com.github.pagehelper.Page;
import com.putra.trade.pojo.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {

    /**
     * 登录：按学号或邮箱查询
     */
    @Select("SELECT * FROM member WHERE student_no = #{account} OR email = #{account}")
    Member getByAccount(@Param("account") String account);

    @Select("SELECT * FROM member WHERE student_no = #{studentNo}")
    Member getByStudentNo(@Param("studentNo") String studentNo);

    @Select("SELECT * FROM member WHERE email = #{email}")
    Member getByEmail(@Param("email") String email);

    @Select("SELECT * FROM member WHERE id = #{id}")
    Member getById(@Param("id") Long id);

    /**
     * 注册插入（useGeneratedKeys 回填 id）
     */
    void insert(Member member);

    /**
     * 个人资料更新（动态 SQL，null 字段不覆盖）
     */
    void update(Member member);

    /**
     * 后台封禁/解禁（null 字段不动）
     */
    void updateBan(@Param("id") Long id, @Param("banPublish") Integer banPublish,
                   @Param("banChat") Integer banChat, @Param("status") Integer status);

    /**
     * 后台用户分页（按学号/昵称模糊）
     */
    Page<Member> pageAdmin(@Param("keyword") String keyword);
}
