package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.MeetSpotDTO;
import com.putra.trade.pojo.entity.MeetSpot;
import com.putra.trade.server.service.MeetSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user/meetSpot")
@RequiredArgsConstructor
@Tag(name = "用户端-面交地点")
public class MeetSpotController {

    private final MeetSpotService meetSpotService;

    @GetMapping("/list")
    @Operation(summary = "我的面交地点列表")
    public Result<List<MeetSpot>> list() {
        return Result.success(meetSpotService.list());
    }

    @PostMapping
    @Operation(summary = "新增面交地点")
    public Result<Void> add(@Valid @RequestBody MeetSpotDTO dto) {
        meetSpotService.add(dto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改面交地点")
    public Result<Void> update(@Valid @RequestBody MeetSpotDTO dto) {
        meetSpotService.update(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除面交地点")
    public Result<Void> delete(@PathVariable Long id) {
        meetSpotService.delete(id);
        return Result.success();
    }
}
