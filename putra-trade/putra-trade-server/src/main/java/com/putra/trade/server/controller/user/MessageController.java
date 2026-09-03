package com.putra.trade.server.controller.user;

import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.server.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/message")
@RequiredArgsConstructor
@Tag(name = "用户端-站内消息")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/page")
    @Operation(summary = "我的消息")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(messageService.page(BaseContext.getCurrentId(), page, pageSize));
    }

    @GetMapping("/unread")
    @Operation(summary = "未读数")
    public Result<Integer> unread() {
        return Result.success(messageService.unread(BaseContext.getCurrentId()));
    }

    @PutMapping("/readAll")
    @Operation(summary = "全部已读")
    public Result<Void> readAll() {
        messageService.readAll(BaseContext.getCurrentId());
        return Result.success();
    }
}
