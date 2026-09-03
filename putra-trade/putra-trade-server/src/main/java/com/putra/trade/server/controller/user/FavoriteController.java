package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.server.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/favorite")
@RequiredArgsConstructor
@Tag(name = "用户端-收藏")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{itemId}")
    @Operation(summary = "收藏")
    public Result<Void> add(@PathVariable Long itemId) {
        favoriteService.add(itemId);
        return Result.success();
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "取消收藏")
    public Result<Void> remove(@PathVariable Long itemId) {
        favoriteService.remove(itemId);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "我的收藏")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(favoriteService.pageMine(page, pageSize));
    }
}
