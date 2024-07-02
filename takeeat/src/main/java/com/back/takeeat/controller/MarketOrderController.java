package com.back.takeeat.controller;

import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.dto.marketorder.request.MarketOrderSearchRequest;
import com.back.takeeat.dto.marketorder.response.DetailMarketOrderResponse;
import com.back.takeeat.dto.marketorder.response.MarketOrdersResponse;
import com.back.takeeat.service.MarketOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarketOrderController {

    private final MarketOrderService marketOrderService;

    @GetMapping("/{marketId}/order")
    public String marketOrder(@PathVariable("marketId") Long marketId, Model model) {
        // TODO : 해당 경로로 접근하는 member가 해당 market의 주인인지 판별할 것(예외처리)
        Map<OrderStatus, Long> marketOrderStatusResponses = marketOrderService.findMarketOrderStatus(marketId);

        model.addAttribute("marketOrderStatus", marketOrderStatusResponses);

        return "market/marketOrder";
    }

    @GetMapping("/{marketId}/orders")
    @ResponseBody
    public List<MarketOrdersResponse> marketOrders(@PathVariable("marketId") Long marketId, @ModelAttribute MarketOrderSearchRequest searchRequest) {
        // TODO : 해당 경로로 접근하는 member가 해당 market의 주인인지 판별할 것(예외처리)
        return marketOrderService.getOrdersByStatusCondition(marketId, searchRequest);
    }

    @GetMapping("/order/{orderId}")
    @ResponseBody
    public DetailMarketOrderResponse getOrderDetail(@PathVariable("orderId") Long orderId) {
        // TODO : 해당 경로로 접근하는 member가 해당 market의 주인인지 판별할 것(예외처리)
        log.info("orderId : {}", orderId);

        return marketOrderService.findDetailMarketOrder(orderId);
    }

}
