package com.putra.trade.server.service;

import com.putra.trade.server.mapper.StatisticMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private static final int TREND_DAYS = 30;

    private final StatisticMapper statisticMapper;

    /** 看板总览 + 近30天趋势 + 分类占比 + 热门商品 */
    public Map<String, Object> dashboard() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalMembers", statisticMapper.countMembers());
        result.put("totalItems", statisticMapper.countItems());
        result.put("completedOrders", statisticMapper.countCompletedOrders());
        result.put("pendingReports", statisticMapper.countPendingReports());
        result.put("pendingAudit", statisticMapper.countPendingAudit());

        LocalDate begin = LocalDate.now().minusDays(TREND_DAYS);
        result.put("memberTrend", statisticMapper.countMembersByDate(begin));
        result.put("itemTrend", statisticMapper.countItemsByDate(begin));
        result.put("reportHandleTrend", statisticMapper.countHandledReportsByDate(begin));
        result.put("categoryDistribution", statisticMapper.countItemsByCategory());
        result.put("topItems", statisticMapper.topItemsByWant());
        return result;
    }

    /** 导出近30天运营报表（xlsx） */
    public byte[] exportReport() {
        LocalDate begin = LocalDate.now().minusDays(TREND_DAYS);
        List<Map<String, Object>> members = statisticMapper.countMembersByDate(begin);
        List<Map<String, Object>> items = statisticMapper.countItemsByDate(begin);
        List<Map<String, Object>> reports = statisticMapper.countHandledReportsByDate(begin);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeSheet(workbook, "每日新增用户", members);
            writeSheet(workbook, "每日新增商品", items);
            writeSheet(workbook, "每日违规处理", reports);
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("报表导出失败", e);
        }
    }

    private void writeSheet(Workbook workbook, String sheetName, List<Map<String, Object>> data) {
        Sheet sheet = workbook.createSheet(sheetName);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("日期");
        header.createCell(1).setCellValue("数量");
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(String.valueOf(data.get(i).get("date")));
            row.createCell(1).setCellValue(((Number) data.get(i).get("count")).doubleValue());
        }
    }
}
