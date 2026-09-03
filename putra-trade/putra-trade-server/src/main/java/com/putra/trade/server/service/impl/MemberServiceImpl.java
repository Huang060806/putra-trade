package com.putra.trade.server.service.impl;

import com.putra.trade.common.constant.MessageConstant;
import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.properties.JwtProperties;
import com.putra.trade.common.utils.BCryptUtil;
import com.putra.trade.common.utils.JwtUtil;
import com.putra.trade.pojo.dto.LoginDTO;
import com.putra.trade.pojo.dto.MemberUpdateDTO;
import com.putra.trade.pojo.dto.RegisterDTO;
import com.putra.trade.pojo.entity.Member;
import com.putra.trade.pojo.vo.MemberLoginVO;
import com.putra.trade.server.interceptor.JwtTokenMemberInterceptor;
import com.putra.trade.server.mapper.MemberMapper;
import com.putra.trade.server.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    /** UPM 校园邮箱允许的后缀 */
    private static final String[] UPM_EMAIL_SUFFIXES = {"@student.upm.edu.my", "@graduate.upm.edu.my", "@upm.edu.my"};

    private final MemberMapper memberMapper;
    private final JwtProperties jwtProperties;

    @Override
    public MemberLoginVO login(LoginDTO loginDTO) {
        Member member = memberMapper.getByAccount(loginDTO.getAccount());

        if (member == null) {
            throw new BusinessException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if (!BCryptUtil.matches(loginDTO.getPassword(), member.getPassword())) {
            throw new BusinessException(MessageConstant.PASSWORD_ERROR);
        }
        if (member.getStatus() == 0) {
            throw new BusinessException(MessageConstant.ACCOUNT_LOCKED);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtTokenMemberInterceptor.CLAIM_MEMBER_ID, member.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getMemberSecretKey(),
                jwtProperties.getMemberTtl(),
                claims);

        log.info("学生登录成功, id={}, studentNo={}", member.getId(), member.getStudentNo());
        return MemberLoginVO.builder()
                .id(member.getId())
                .studentNo(member.getStudentNo())
                .nickname(member.getNickname())
                .avatar(member.getAvatar())
                .token(token)
                .build();
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        if (!isUpmEmail(registerDTO.getEmail())) {
            throw new BusinessException(MessageConstant.EMAIL_NOT_UPM);
        }
        if (memberMapper.getByStudentNo(registerDTO.getStudentNo()) != null) {
            throw new BusinessException(MessageConstant.STUDENT_NO_EXISTS);
        }
        if (memberMapper.getByEmail(registerDTO.getEmail()) != null) {
            throw new BusinessException(MessageConstant.EMAIL_EXISTS);
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException(MessageConstant.PASSWORD_NOT_MATCH);
        }

        Member member = Member.builder()
                .studentNo(registerDTO.getStudentNo())
                .email(registerDTO.getEmail())
                .password(BCryptUtil.hash(registerDTO.getPassword()))
                .nickname(registerDTO.getNickname() != null && !registerDTO.getNickname().isEmpty()
                        ? registerDTO.getNickname() : registerDTO.getStudentNo())
                .gender(0)
                .banPublish(0)
                .banChat(0)
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        memberMapper.insert(member);
        log.info("学生注册成功, id={}, studentNo={}", member.getId(), member.getStudentNo());
    }

    @Override
    public void updateProfile(MemberUpdateDTO updateDTO) {
        Member member = new Member();
        BeanUtils.copyProperties(updateDTO, member);
        member.setId(BaseContext.getCurrentId());
        member.setUpdateTime(LocalDateTime.now());
        memberMapper.update(member);
        log.info("更新个人资料, id={}", member.getId());
    }

    private boolean isUpmEmail(String email) {
        if (email == null) {
            return false;
        }
        for (String suffix : UPM_EMAIL_SUFFIXES) {
            if (email.toLowerCase().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
