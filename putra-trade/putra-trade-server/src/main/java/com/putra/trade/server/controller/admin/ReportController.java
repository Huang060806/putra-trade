package com.putra.trade.server.controller.admin;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.ReportHandleDTO;
import com.putra.trade.server.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("adminReportController")
@RequestMapping("/admin/report")
@RequiredArgsConstructor
@Tag(name = "管理端-举报审核")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/page")
    @Operation(summary = "举报分页（targetType/status 可筛）")
    public Result<PageResult> page(@RequestParam(required = false) Integer targetType,
                                   @RequestParam(required = false) Integer status,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(reportService.page(targetType, status, page, pageSize));
    }

    @PutMapping("/handle")
    @Operation(summary = "处理举报（DISMISS/TAKEDOWN/BAN_PUBLISH/BAN_CHAT/BAN_ACCOUNT）")
    public Result<Void> handle(@Valid @RequestBody ReportHandleDTO dto) {
        reportService.handle(dto);
        return Result.success();
    }
}
