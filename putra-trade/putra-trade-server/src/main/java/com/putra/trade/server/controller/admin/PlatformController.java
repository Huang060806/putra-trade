package com.putra.trade.server.controller.admin;

import com.putra.trade.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/platform")
@RequiredArgsConstructor
@Tag(name = "管理端-平台状态")
public class PlatformController {

    private static final String KEY = "putra:platform:status";

    private final RedisTemplate<String, Object> redisTemplate;

    @PutMapping("/{status}")
    @Operation(summary = "设置平台状态（1正常 0维护）")
    public Result<Void> setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    @Operation(summary = "查询平台状态")
    public Result<Integer> getStatus() {
        Object value = redisTemplate.opsForValue().get(KEY);
        return Result.success(value == null ? 1 : Integer.valueOf(String.valueOf(value)));
    }
}
