package pl.prz.mnykolaichuk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.prz.mnykolaichuk.model.dto.UserDto;
import pl.prz.mnykolaichuk.service.AdminOrderService;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {
    private final AdminOrderService adminOrderService;
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> getUserOrderId(@RequestParam Long orderId,
                                                  @RequestHeader HttpHeaders headers) {
        return adminOrderService.getUserByOrderId(orderId, headers.getFirst("Authorization"));
    }
}
