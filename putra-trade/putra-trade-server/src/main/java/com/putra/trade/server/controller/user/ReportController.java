package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.ReportSubmitDTO;
import com.putra.trade.server.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("userReportController")
@RequestMapping("/user/report")
@RequiredArgsConstructor
@Tag(name = "用户端-举报")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "提交举报（targetType: 1商品 2用户）")
    public Result<Void> submit(@Valid @RequestBody ReportSubmitDTO dto) {
        reportService.submit(dto);
        return Result.success();
    }
}
