package com.putra.trade.server.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.entity.Item;
import com.putra.trade.pojo.vo.ItemDetailVO;
import com.putra.trade.server.mapper.FavoriteMapper;
import com.putra.trade.server.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final ItemMapper itemMapper;

    public void add(Long itemId) {
        Item item = itemMapper.getById(itemId);
        if (item == null) {
            throw new BusinessException("商品不存在");
        }
        favoriteMapper.insert(BaseContext.getCurrentId(), itemId);
    }

    public void remove(Long itemId) {
        favoriteMapper.delete(BaseContext.getCurrentId(), itemId);
    }

    public PageResult pageMine(int page, int pageSize) {
        Page<ItemDetailVO> p = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> favoriteMapper.pageByMember(BaseContext.getCurrentId()));
        return new PageResult(p.getTotal(), p.getResult());
    }
}
