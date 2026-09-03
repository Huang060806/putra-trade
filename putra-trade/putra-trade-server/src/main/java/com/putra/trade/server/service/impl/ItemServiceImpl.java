package com.putra.trade.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.dto.ItemDTO;
import com.putra.trade.pojo.dto.ItemPageQueryDTO;
import com.putra.trade.pojo.entity.Item;
import com.putra.trade.pojo.entity.Member;
import com.putra.trade.pojo.vo.ItemDetailVO;
import com.putra.trade.server.mapper.FavoriteMapper;
import com.putra.trade.server.mapper.ItemImageMapper;
import com.putra.trade.server.mapper.ItemMapper;
import com.putra.trade.server.mapper.MemberMapper;
import com.putra.trade.server.mapper.WantMapper;
import com.putra.trade.server.service.ItemService;
import com.putra.trade.server.service.MessageService;
import com.putra.trade.server.service.SensitiveWordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    public static final int STATUS_REMOVED = 0;
    public static final int STATUS_ON_SALE = 1;
    public static final int STATUS_RESERVED = 2;
    public static final int STATUS_SOLD = 3;
    public static final int STATUS_PENDING_AUDIT = 4;

    private static final String KEY_PLATFORM_STATUS = "putra:platform:status";
    private static final String KEY_HOT_PAGE = "putra:items:hot:first";

    private final ItemMapper itemMapper;
    private final ItemImageMapper itemImageMapper;
    private final MemberMapper memberMapper;
    private final WantMapper wantMapper;
    private final FavoriteMapper favoriteMapper;
    private final SensitiveWordService sensitiveWordService;
    private final MessageService messageService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void publish(ItemDTO dto) {
        Member seller = currentMember();
        if (seller.getBanPublish() == 1) {
            throw new BusinessException("你已被禁止发布商品，请联系管理员");
        }

        Item item = new Item();
        BeanUtils.copyProperties(dto, item);
        if (item.getConditionLevel() == null) {
            item.setConditionLevel(3);
        }
        item.setSellerId(seller.getId());
        item.setCreateUser(seller.getId());
        item.setUpdateUser(seller.getId());
        applyAudit(item, dto.getTitle() + " " + (dto.getDescription() == null ? "" : dto.getDescription()));
        itemMapper.insert(item);

        saveImages(item.getId(), dto.getImages());
        evictHotCache();
        log.info("商品发布, id={}, status={}", item.getId(), item.getStatus());
    }

    @Override
    @Transactional
    public void update(ItemDTO dto) {
        Item item = checkOwner(dto.getId());
        BeanUtils.copyProperties(dto, item);
        item.setUpdateUser(BaseContext.getCurrentId());
        applyAudit(item, dto.getTitle() + " " + (dto.getDescription() == null ? "" : dto.getDescription()));
        itemMapper.update(item);

        itemImageMapper.deleteByItemId(item.getId());
        saveImages(item.getId(), dto.getImages());
        evictHotCache();
    }

    @Override
    public PageResult pageForUser(ItemPageQueryDTO query) {
        Object platformStatus = redisTemplate.opsForValue().get(KEY_PLATFORM_STATUS);
        if ("0".equals(String.valueOf(platformStatus))) {
            throw new BusinessException("平台维护中，请稍后再来");
        }
        query.setStatus(STATUS_ON_SALE);

        // 热度排序首页：Redis 缓存 5 分钟（高频访问，want_count 变动时主动失效）
        boolean hotFirstPage = "hot".equals(query.getSortBy()) && query.getPage() == 1
                && query.getKeyword() == null && query.getCategoryId() == null && query.getCampusArea() == null;
        if (hotFirstPage) {
            Object cached = redisTemplate.opsForValue().get(KEY_HOT_PAGE);
            if (cached != null) {
                return objectMapper.convertValue(cached, PageResult.class);
            }
        }

        Page<ItemDetailVO> p = PageHelper.startPage(query.getPage(), query.getPageSize())
                .doSelectPage(() -> itemMapper.pageQuery(query));
        PageResult result = new PageResult(p.getTotal(), p.getResult());
        if (hotFirstPage) {
            redisTemplate.opsForValue().set(KEY_HOT_PAGE, result, 5, TimeUnit.MINUTES);
        }
        return result;
    }

    @Override
    public PageResult pageForAdmin(ItemPageQueryDTO query) {
        Page<ItemDetailVO> p = PageHelper.startPage(query.getPage(), query.getPageSize())
                .doSelectPage(() -> itemMapper.pageQuery(query));
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public PageResult pageMine(ItemPageQueryDTO query) {
        query.setSellerId(BaseContext.getCurrentId());
        Page<ItemDetailVO> p = PageHelper.startPage(query.getPage(), query.getPageSize())
                .doSelectPage(() -> itemMapper.pageQuery(query));
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public ItemDetailVO detail(Long id) {
        ItemDetailVO vo = itemMapper.getDetailById(id);
        if (vo == null) {
            throw new BusinessException("商品不存在");
        }
        itemMapper.incrViewCount(id);
        vo.setImages(itemImageMapper.getUrlsByItemId(id));

        Long currentId = BaseContext.getCurrentId();
        if (currentId != null) {
            vo.setWanted(wantMapper.count(currentId, id) > 0);
            vo.setFavorited(favoriteMapper.count(currentId, id) > 0);
        }
        return vo;
    }

    @Override
    public void markSold(Long id) {
        Item item = checkOwner(id);
        if (item.getStatus() != STATUS_ON_SALE && item.getStatus() != STATUS_RESERVED) {
            throw new BusinessException("当前状态不能标记为已售出");
        }
        itemMapper.updateStatus(id, STATUS_SOLD);
        evictHotCache();
    }

    @Override
    public void relist(Long id) {
        Item item = checkOwner(id);
        if (item.getStatus() != STATUS_REMOVED) {
            throw new BusinessException("只有已下架的商品才能重新上架");
        }
        itemMapper.updateStatus(id, STATUS_ON_SALE);
        evictHotCache();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkOwner(id);
        itemImageMapper.deleteByItemId(id);
        itemMapper.deleteById(id);
        evictHotCache();
    }

    @Override
    public void audit(Long id, boolean pass, String remark) {
        Item item = itemMapper.getById(id);
        if (item == null || item.getStatus() != STATUS_PENDING_AUDIT) {
            throw new BusinessException("该商品不在待审核状态");
        }
        itemMapper.updateStatus(id, pass ? STATUS_ON_SALE : STATUS_REMOVED);
        if (!pass) {
            messageService.send(item.getSellerId(), MessageService.TYPE_PLATFORM,
                    "你的商品「" + item.getTitle() + "」未通过审核：" + remark, null);
        }
        evictHotCache();
    }

    @Override
    public void takedown(Long id, String reason) {
        Item item = itemMapper.getById(id);
        if (item == null) {
            throw new BusinessException("商品不存在");
        }
        itemMapper.updateStatus(id, STATUS_REMOVED);
        messageService.send(item.getSellerId(), MessageService.TYPE_PLATFORM,
                "你的商品「" + item.getTitle() + "」因违规已被下架。原因：" + reason, null);
        evictHotCache();
    }

    /** 敏感词扫描：干净 → 在售；命中 → 待审核并记录命中词 */
    private void applyAudit(Item item, String text) {
        List<String> hits = sensitiveWordService.scan(text);
        if (hits.isEmpty()) {
            item.setStatus(STATUS_ON_SALE);
            item.setAuditRemark(null);
        } else {
            item.setStatus(STATUS_PENDING_AUDIT);
            item.setAuditRemark("命中敏感词: " + String.join(",", hits));
        }
    }

    private void saveImages(Long itemId, List<String> images) {
        if (images == null) {
            return;
        }
        for (int i = 0; i < images.size(); i++) {
            itemImageMapper.insert(itemId, images.get(i), i + 1);
        }
    }

    private Item checkOwner(Long id) {
        Item item = itemMapper.getById(id);
        if (item == null) {
            throw new BusinessException("商品不存在");
        }
        if (!item.getSellerId().equals(BaseContext.getCurrentId())) {
            throw new BusinessException("只能操作自己的商品");
        }
        return item;
    }

    private Member currentMember() {
        Member member = memberMapper.getById(BaseContext.getCurrentId());
        if (member == null) {
            throw new BusinessException("用户不存在");
        }
        return member;
    }

    private void evictHotCache() {
        redisTemplate.delete(KEY_HOT_PAGE);
    }
}
