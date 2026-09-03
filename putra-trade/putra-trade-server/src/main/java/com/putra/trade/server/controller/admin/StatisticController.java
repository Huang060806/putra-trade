package com.putra.trade.server.controller.admin;

import com.putra.trade.common.result.Result;
import com.putra.trade.server.service.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/admin/statistic")
@RequiredArgsConstructor
@Tag(name = "管理端-数据统计")
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/dashboard")
    @Operation(summary = "数据看板（总览+30天趋势+分类占比+热门商品）")
    public Result<Map<String, Object>> dashboard() {
        return Result.success(statisticService.dashboard());
    }

    @GetMapping("/export")
    @Operation(summary = "导出近30天运营报表（xlsx）")
    public ResponseEntity<byte[]> export() {
        byte[] data = statisticService.exportReport();
        String filename = URLEncoder.encode("putra-trade-report-" + LocalDate.now() + ".xlsx", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}
