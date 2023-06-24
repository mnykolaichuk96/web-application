package pl.prz.mnykolaichuk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.prz.mnykolaichuk.model.dto.UserDto;
import pl.prz.mnykolaichuk.repository.OrderRepository;
import pl.prz.mnykolaichuk.service.rest.client.AdminOrderFeignClient;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AdminOrderService {
    private final OrderRepository orderRepository;
    private final AdminOrderFeignClient adminOrderFeignClient;

    public ResponseEntity<UserDto> getUserByOrderId(Long orderId, String jwt) {

        String userId = orderRepository.getOrderById(orderId).getUserId();

        HashMap<String, Object> response = adminOrderFeignClient.getUserInfo(userId, jwt);
        UserDto userDto = UserDto.builder()
                .username(response.getOrDefault("username", "").toString())
                .firstname(response.getOrDefault("firstName", "").toString())
                .lastname(response.getOrDefault("lastName", "").toString())
                .email(response.getOrDefault("email", "").toString())
                .emailVerified(
                        Boolean.parseBoolean(response.getOrDefault("emailVerified", "").toString()))
                .build();

        return ResponseEntity.ok().body(userDto);
    }

}
