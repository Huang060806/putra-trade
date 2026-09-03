package com.putra.trade.server.service;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.dto.ItemDTO;
import com.putra.trade.pojo.dto.ItemPageQueryDTO;
import com.putra.trade.pojo.vo.ItemDetailVO;

public interface ItemService {

    /** 发布商品：敏感词扫描 → 干净直接在售，命中进待审核 */
    void publish(ItemDTO dto);

    /** 编辑商品（仅卖家本人，重新走敏感词扫描） */
    void update(ItemDTO dto);

    /** 用户端分页（强制 status=1 在售；hot 排序首页走 Redis 缓存） */
    PageResult pageForUser(ItemPageQueryDTO query);

    /** 管理端分页（可传任意 status） */
    PageResult pageForAdmin(ItemPageQueryDTO query);

    /** 我的发布（卖家视角，含全部状态） */
    PageResult pageMine(ItemPageQueryDTO query);

    /** 详情：浏览量+1，标记当前用户是否已想要/收藏 */
    ItemDetailVO detail(Long id);

    /** 卖家标记已售出 */
    void markSold(Long id);

    /** 卖家重新上架（已下架 → 在售） */
    void relist(Long id);

    /** 卖家删除 */
    void delete(Long id);

    /** 管理端：审核待审商品（pass=true → 在售，false → 下架） */
    void audit(Long id, boolean pass, String remark);

    /** 管理端：违规下架（必填理由，通知卖家） */
    void takedown(Long id, String reason);
}
