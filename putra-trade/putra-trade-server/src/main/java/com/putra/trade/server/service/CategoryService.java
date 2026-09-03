package com.putra.trade.server.service;

import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.pojo.dto.CategoryDTO;
import com.putra.trade.pojo.entity.Category;
import com.putra.trade.server.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<Category> list() {
        return categoryMapper.list();
    }

    public void add(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insert(category);
    }

    public void update(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    public void delete(Long id) {
        if (categoryMapper.countItemByCategoryId(id) > 0) {
            throw new BusinessException("该分类下还有商品，不能删除");
        }
        categoryMapper.deleteById(id);
    }
}
