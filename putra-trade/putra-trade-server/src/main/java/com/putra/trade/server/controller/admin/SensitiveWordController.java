package com.putra.trade.server.controller.admin;

import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.entity.SensitiveWord;
import com.putra.trade.server.service.SensitiveWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sensitiveWord")
@RequiredArgsConstructor
@Tag(name = "管理端-敏感词库")
public class SensitiveWordController {

    private final SensitiveWordService sensitiveWordService;

    @GetMapping("/list")
    @Operation(summary = "词库列表")
    public Result<List<SensitiveWord>> list() {
        return Result.success(sensitiveWordService.list());
    }

    @PostMapping
    @Operation(summary = "新增敏感词（即时生效）")
    public Result<Void> add(@RequestParam String word, @RequestParam(defaultValue = "3") Integer type) {
        sensitiveWordService.add(word, type);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除敏感词")
    public Result<Void> delete(@PathVariable Long id) {
        sensitiveWordService.delete(id);
        return Result.success();
    }
}
