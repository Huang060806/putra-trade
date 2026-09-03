package com.putra.trade.server.controller.admin;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.ItemPageQueryDTO;
import com.putra.trade.server.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("adminItemController")
@RequestMapping("/admin/item")
@RequiredArgsConstructor
@Tag(name = "管理端-商品管理")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/page")
    @Operation(summary = "商品分页（可按状态/关键字筛选）")
    public Result<PageResult> page(ItemPageQueryDTO query) {
        return Result.success(itemService.pageForAdmin(query));
    }

    @PutMapping("/audit/{id}")
    @Operation(summary = "审核待审商品（pass=true 上架 / false 驳回下架）")
    public Result<Void> audit(@PathVariable Long id, @RequestParam boolean pass,
                              @RequestParam(required = false, defaultValue = "内容违规") String remark) {
        itemService.audit(id, pass, remark);
        return Result.success();
    }

    @PutMapping("/takedown/{id}")
    @Operation(summary = "违规下架（必填理由，通知卖家）")
    public Result<Void> takedown(@PathVariable Long id, @RequestParam String reason) {
        itemService.takedown(id, reason);
        return Result.success();
    }
}
