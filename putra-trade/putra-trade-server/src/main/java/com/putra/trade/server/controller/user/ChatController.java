package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.ChatSendDTO;
import com.putra.trade.pojo.entity.ChatMessage;
import com.putra.trade.pojo.vo.ChatSessionVO;
import com.putra.trade.server.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user/chat")
@RequiredArgsConstructor
@Tag(name = "用户端-私聊")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    @Operation(summary = "发送私聊消息")
    public Result<ChatMessage> send(@Valid @RequestBody ChatSendDTO dto) {
        return Result.success(chatService.send(dto));
    }

    @GetMapping("/sessions")
    @Operation(summary = "会话列表（最后一条消息+未读数）")
    public Result<List<ChatSessionVO>> sessions() {
        return Result.success(chatService.sessions());
    }

    @GetMapping("/history")
    @Operation(summary = "与某人的聊天记录（正序）")
    public Result<PageResult> history(@RequestParam Long peerId,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "20") int pageSize) {
        return Result.success(chatService.history(peerId, page, pageSize));
    }

    @GetMapping("/unread")
    @Operation(summary = "私聊未读总数")
    public Result<Integer> unread() {
        return Result.success(chatService.unread());
    }
}
