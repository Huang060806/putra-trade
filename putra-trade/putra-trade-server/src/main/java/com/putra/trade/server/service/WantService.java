package com.putra.trade.server.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.entity.Item;
import com.putra.trade.pojo.entity.Member;
import com.putra.trade.pojo.vo.ItemDetailVO;
import com.putra.trade.pojo.vo.WantAddVO;
import com.putra.trade.server.mapper.ItemMapper;
import com.putra.trade.server.mapper.MemberMapper;
import com.putra.trade.server.mapper.WantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WantService {

    private final WantMapper wantMapper;
    private final ItemMapper itemMapper;
    private final MemberMapper memberMapper;
    private final MessageService messageService;

    /**
     * 点"我想要"：防刷（不能想要自己的商品 + 唯一索引幂等），
     * 成功后返回卖家联系方式（决策⑨-b / ⑰）
     */
    @Transactional
    public WantAddVO add(Long itemId) {
        Long memberId = BaseContext.getCurrentId();
        Item item = itemMapper.getById(itemId);
        if (item == null || item.getStatus() == 0) {
            throw new BusinessException("商品不存在或已下架");
        }
        if (item.getSellerId().equals(memberId)) {
            throw new BusinessException("不能想要自己的商品");
        }

        // INSERT IGNORE + 唯一索引：重复点不报错也不重复计数（幂等）
        int rows = wantMapper.insert(memberId, itemId);
        if (rows > 0) {
            itemMapper.incrWantCount(itemId);
            Member buyer = memberMapper.getById(memberId);
            messageService.send(item.getSellerId(), MessageService.TYPE_WANT,
                    "有人想要你的商品「" + item.getTitle() + "」(" + buyer.getNickname() + ")，快去联系TA吧！", null);
        }

        Member seller = memberMapper.getById(item.getSellerId());
        return WantAddVO.builder()
                .sellerNickname(seller.getNickname())
                .wechat(seller.getWechat())
                .whatsapp(seller.getWhatsapp())
                .phone(seller.getPhone())
                .build();
    }

    @Transactional
    public void remove(Long itemId) {
        int rows = wantMapper.delete(BaseContext.getCurrentId(), itemId);
        if (rows > 0) {
            itemMapper.decrWantCount(itemId);
        }
    }

    public PageResult pageMine(int page, int pageSize) {
        Page<ItemDetailVO> p = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> wantMapper.pageByMember(BaseContext.getCurrentId()));
        return new PageResult(p.getTotal(), p.getResult());
    }
}
