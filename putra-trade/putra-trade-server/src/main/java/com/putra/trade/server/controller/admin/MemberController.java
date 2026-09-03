package com.putra.trade.server.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.UserBanDTO;
import com.putra.trade.pojo.entity.Member;
import com.putra.trade.server.mapper.MemberMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("adminMemberController")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
@Tag(name = "管理端-用户管理")
public class MemberController {

    private final MemberMapper memberMapper;

    @GetMapping("/page")
    @Operation(summary = "用户分页（学号/昵称模糊）")
    public Result<PageResult> page(@RequestParam(required = false) String keyword,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        Page<Member> p = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> memberMapper.pageAdmin(keyword));
        p.getResult().forEach(m -> m.setPassword(null));
        return Result.success(new PageResult(p.getTotal(), p.getResult()));
    }

    @PutMapping("/ban")
    @Operation(summary = "封禁/解禁（banPublish/banChat/status 传啥改啥）")
    public Result<Void> ban(@Valid @RequestBody UserBanDTO dto) {
        memberMapper.updateBan(dto.getMemberId(), dto.getBanPublish(), dto.getBanChat(), dto.getStatus());
        return Result.success();
    }
}
