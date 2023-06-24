package pl.prz.mnykolaichuk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.prz.mnykolaichuk.model.dto.OrderRequest;
import pl.prz.mnykolaichuk.model.dto.response.OrderResponse;
import pl.prz.mnykolaichuk.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping()
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest,
                                                                @RequestHeader HttpHeaders headers) {
        return orderService.placeOrder(orderRequest, headers.getFirst("userId"));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getOrderListByUserId(@RequestHeader HttpHeaders headers) {
        return orderService.getOrderListByUserId(headers.getFirst("userId"));
    }
}
