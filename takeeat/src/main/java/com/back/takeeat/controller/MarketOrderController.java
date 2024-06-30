package com.back.takeeat.controller;

import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.dto.marketorder.response.MarketOrdersResponse;
import com.back.takeeat.service.MarketOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarketOrderController {

    private final MarketOrderService marketOrderService;

    @GetMapping("/{marketId}/order")
    public String marketOrder(@PathVariable("marketId") Long marketId, Model model) {
        // TODO : 해당 경로로 접근하는 member가 해당 market의 주인인지 판별할 것(예외처리)
        Map<OrderStatus, Long> marketOrderStatusResponses = marketOrderService.findMarketOrderStatus(marketId);
        List<MarketOrdersResponse> marketOrdersResponses = marketOrderService.getOrdersByStatusCondition(marketId, OrderStatus.WAIT);

        model.addAttribute("marketOrderStatus", marketOrderStatusResponses);
        model.addAttribute("marketOrdersResponses", marketOrdersResponses);

        return "market/marketOrder";
    }

}
