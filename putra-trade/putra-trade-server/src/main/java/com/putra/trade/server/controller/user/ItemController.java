package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.ItemDTO;
import com.putra.trade.pojo.dto.ItemPageQueryDTO;
import com.putra.trade.pojo.vo.ItemDetailVO;
import com.putra.trade.server.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("userItemController")
@RequestMapping("/user/item")
@RequiredArgsConstructor
@Tag(name = "用户端-商品")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "发布商品（敏感词命中自动进待审核）")
    public Result<Void> publish(@Valid @RequestBody ItemDTO dto) {
        itemService.publish(dto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "编辑商品")
    public Result<Void> update(@Valid @RequestBody ItemDTO dto) {
        itemService.update(dto);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "商品分页（keyword/categoryId/campusArea + latest|price_asc|price_desc|hot）")
    public Result<PageResult> page(ItemPageQueryDTO query) {
        return Result.success(itemService.pageForUser(query));
    }

    @GetMapping("/mine")
    @Operation(summary = "我的发布")
    public Result<PageResult> mine(ItemPageQueryDTO query) {
        return Result.success(itemService.pageMine(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "商品详情")
    public Result<ItemDetailVO> detail(@PathVariable Long id) {
        return Result.success(itemService.detail(id));
    }

    @PutMapping("/sold/{id}")
    @Operation(summary = "标记为已售出")
    public Result<Void> markSold(@PathVariable Long id) {
        itemService.markSold(id);
        return Result.success();
    }

    @PutMapping("/relist/{id}")
    @Operation(summary = "重新上架")
    public Result<Void> relist(@PathVariable Long id) {
        itemService.relist(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    public Result<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return Result.success();
    }
}
