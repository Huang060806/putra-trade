package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.vo.WantAddVO;
import com.putra.trade.server.service.WantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/want")
@RequiredArgsConstructor
@Tag(name = "用户端-想要")
public class WantController {

    private final WantService wantService;

    @PostMapping("/{itemId}")
    @Operation(summary = "点\"我想要\"（返回卖家联系方式，幂等）")
    public Result<WantAddVO> add(@PathVariable Long itemId) {
        return Result.success(wantService.add(itemId));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "取消想要")
    public Result<Void> remove(@PathVariable Long itemId) {
        wantService.remove(itemId);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "我想要的列表（含商品实时状态）")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(wantService.pageMine(page, pageSize));
    }
}
