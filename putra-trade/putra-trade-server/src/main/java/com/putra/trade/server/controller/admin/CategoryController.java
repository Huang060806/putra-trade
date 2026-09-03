package com.putra.trade.server.controller.admin;

import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.CategoryDTO;
import com.putra.trade.pojo.entity.Category;
import com.putra.trade.server.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@RequiredArgsConstructor
@Tag(name = "管理端-分类管理")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "分类列表")
    public Result<List<Category>> list() {
        return Result.success(categoryService.list());
    }

    @PostMapping
    @Operation(summary = "新增分类")
    public Result<Void> add(@Valid @RequestBody CategoryDTO dto) {
        categoryService.add(dto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改分类")
    public Result<Void> update(@Valid @RequestBody CategoryDTO dto) {
        categoryService.update(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类（下有商品时禁止）")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }
}
