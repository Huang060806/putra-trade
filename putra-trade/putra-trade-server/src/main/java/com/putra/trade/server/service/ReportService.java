package com.putra.trade.server.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.pojo.dto.ReportHandleDTO;
import com.putra.trade.pojo.dto.ReportSubmitDTO;
import com.putra.trade.pojo.entity.Item;
import com.putra.trade.pojo.entity.Member;
import com.putra.trade.pojo.entity.Report;
import com.putra.trade.server.mapper.ItemMapper;
import com.putra.trade.server.mapper.MemberMapper;
import com.putra.trade.server.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportMapper reportMapper;
    private final ItemMapper itemMapper;
    private final MemberMapper memberMapper;
    private final MessageService messageService;

    public void submit(ReportSubmitDTO dto) {
        if (dto.getTargetType() == 1) {
            if (itemMapper.getById(dto.getTargetId()) == null) {
                throw new BusinessException("被举报的商品不存在");
            }
        } else if (dto.getTargetType() == 2) {
            if (memberMapper.getById(dto.getTargetId()) == null) {
                throw new BusinessException("被举报的用户不存在");
            }
        } else {
            throw new BusinessException("举报对象类型不正确");
        }

        Report report = Report.builder()
                .reporterId(BaseContext.getCurrentId())
                .targetType(dto.getTargetType())
                .targetId(dto.getTargetId())
                .reason(dto.getReason())
                .images(dto.getImages() == null ? null : String.join(",", dto.getImages()))
                .build();
        reportMapper.insert(report);
    }

    public PageResult page(Integer targetType, Integer status, int page, int pageSize) {
        Page<Report> p = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> reportMapper.pageQuery(targetType, status));
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 处理举报：动作驱动（下架/封禁/驳回），处理结果同步通知被举报人
     */
    @Transactional
    public void handle(ReportHandleDTO dto) {
        Report report = reportMapper.getById(dto.getReportId());
        if (report == null || report.getStatus() != 0) {
            throw new BusinessException("该举报不存在或已处理");
        }

        switch (dto.getAction()) {
            case "DISMISS":
                break;
            case "TAKEDOWN":
                Item item = itemMapper.getById(report.getTargetId());
                if (item != null) {
                    itemMapper.updateStatus(item.getId(), 0);
                    messageService.send(item.getSellerId(), MessageService.TYPE_PLATFORM,
                            "你的商品「" + item.getTitle() + "」经举报核实已被下架。原因：" + dto.getHandleResult(), null);
                }
                break;
            case "BAN_PUBLISH":
            case "BAN_CHAT":
            case "BAN_ACCOUNT":
                applyBan(report.getTargetId(), dto.getAction(), dto.getHandleResult());
                break;
            default:
                throw new BusinessException("不支持的处理动作");
        }

        report.setStatus("DISMISS".equals(dto.getAction()) ? 2 : 1);
        report.setHandleResult(dto.getHandleResult());
        report.setHandlerId(BaseContext.getCurrentId());
        reportMapper.handle(report);
    }

    private void applyBan(Long memberId, String action, String reason) {
        Member member = memberMapper.getById(memberId);
        if (member == null) {
            return;
        }
        switch (action) {
            case "BAN_PUBLISH":
                memberMapper.updateBan(memberId, 1, null, null);
                messageService.send(memberId, MessageService.TYPE_PLATFORM,
                        "你已被禁止发布商品。原因：" + reason, null);
                break;
            case "BAN_CHAT":
                memberMapper.updateBan(memberId, null, 1, null);
                messageService.send(memberId, MessageService.TYPE_PLATFORM,
                        "你已被禁止私聊。原因：" + reason, null);
                break;
            case "BAN_ACCOUNT":
                memberMapper.updateBan(memberId, null, null, 0);
                break;
        }
    }
}
