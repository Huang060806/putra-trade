package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.entity.Category;
import com.putra.trade.server.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@RequiredArgsConstructor
@Tag(name = "用户端-分类")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "分类列表")
    public Result<List<Category>> list() {
        return Result.success(categoryService.list());
    }
}
