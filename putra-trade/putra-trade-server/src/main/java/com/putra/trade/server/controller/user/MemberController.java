package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.LoginDTO;
import com.putra.trade.pojo.dto.MemberUpdateDTO;
import com.putra.trade.pojo.dto.RegisterDTO;
import com.putra.trade.pojo.vo.MemberLoginVO;
import com.putra.trade.server.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户端 - 认证与个人资料
 */
@Slf4j
@RestController("userMemberController")
@RequestMapping("/user/auth")
@RequiredArgsConstructor
@Tag(name = "用户端-认证接口", description = "登录、注册、个人资料")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "学生登录", description = "学号或邮箱 + 密码，返回 JWT Token")
    public Result<MemberLoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("学生登录: {}", loginDTO.getAccount());
        return Result.success(memberService.login(loginDTO));
    }

    @PostMapping("/register")
    @Operation(summary = "学生注册", description = "UPM 邮箱 + 学号双唯一校验")
    public Result<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        log.info("学生注册: studentNo={}, email={}", registerDTO.getStudentNo(), registerDTO.getEmail());
        memberService.register(registerDTO);
        return Result.success();
    }

    @PutMapping("/profile")
    @Operation(summary = "修改个人资料", description = "昵称/头像/联系方式/宿舍区域，需携带 Token")
    public Result<Void> updateProfile(@RequestBody MemberUpdateDTO updateDTO) {
        memberService.updateProfile(updateDTO);
        return Result.success();
    }
}
