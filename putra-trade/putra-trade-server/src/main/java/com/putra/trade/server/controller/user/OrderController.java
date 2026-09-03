package com.putra.trade.server.controller.user;

import com.putra.trade.common.result.PageResult;
import com.putra.trade.common.result.Result;
import com.putra.trade.pojo.dto.OrdersSubmitDTO;
import com.putra.trade.pojo.vo.OrderVO;
import com.putra.trade.server.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/order")
@RequiredArgsConstructor
@Tag(name = "用户端-订单")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/submit")
    @Operation(summary = "下单（锁定商品为交易中）")
    public Result<OrderVO> submit(@Valid @RequestBody OrdersSubmitDTO dto) {
        return Result.success(orderService.submit(dto));
    }

    @PutMapping("/pay/{orderId}")
    @Operation(summary = "模拟支付")
    public Result<Void> pay(@PathVariable Long orderId) {
        orderService.pay(orderId);
        return Result.success();
    }

    @PutMapping("/confirm/{orderId}")
    @Operation(summary = "确认面交完成")
    public Result<Void> confirm(@PathVariable Long orderId) {
        orderService.confirm(orderId);
        return Result.success();
    }

    @PutMapping("/cancel/{orderId}")
    @Operation(summary = "取消订单（释放商品）")
    public Result<Void> cancel(@PathVariable Long orderId, @RequestParam String reason) {
        orderService.cancel(orderId, reason);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "我的订单（买/卖双向）")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(orderService.pageMine(page, pageSize));
    }
}
