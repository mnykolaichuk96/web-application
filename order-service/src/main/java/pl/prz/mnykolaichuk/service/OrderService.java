package pl.prz.mnykolaichuk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.prz.mnykolaichuk.model.dto.OrderLineItemsDto;
import pl.prz.mnykolaichuk.model.dto.OrderRequest;
import pl.prz.mnykolaichuk.model.dto.request.InventoryRequest;
import pl.prz.mnykolaichuk.model.dto.response.OrderResponse;
import pl.prz.mnykolaichuk.model.dto.response.QuantityResponse;
import pl.prz.mnykolaichuk.model.entity.Order;
import pl.prz.mnykolaichuk.model.mapper.OrderLineItemsMapper;
import pl.prz.mnykolaichuk.repository.OrderRepository;
import pl.prz.mnykolaichuk.service.rest.client.InventoryFeignClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * За допомогою анотації @Transactional вказується, що всі методи класу OrderService повинні бути виконані
 * в межах однієї транзакції бази даних. Це означає, що якщо будь-який з методів виконує декілька операцій з
 * базою даних, то ці операції будуть виконані атомарно: або всі успішно виконаються, або жодна з них не буде
 * збережена. Крім того, в разі виникнення виключення в будь-якому з методів, транзакція буде автоматично
 * відкочена (rollback), що забезпечує стабільність та цілісність даних в базі даних.
 * */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    private final OrderLineItemsMapper orderLineItemsMapper = new OrderLineItemsMapper();
    private final InventoryFeignClient inventoryFeignClient;
    private final OrderRepository orderRepository;
    private final MessageSource messageSource;
    public ResponseEntity<String> placeOrder(OrderRequest orderRequest, String userId) {
        List<OrderLineItemsDto> orderLineItemsDtoList = orderRequest.getOrderLineItemsDtoList();
        List<String> skuCodeList = orderLineItemsDtoList.stream()
                .map(OrderLineItemsDto::getSkuCode)
                .toList();
        // get request to inventory-service for getting quantity of every article in order
        List<QuantityResponse> quantityResponseList = inventoryFeignClient.getQuantity(skuCodeList);

        //check is everything in  stock, if not return badRequest with message and skuCodes
        List<String> unavailabeSkuCodeList = new ArrayList<>();
        IntStream.range(0, quantityResponseList.size())
                .filter(i -> quantityResponseList.get(i).getQuantity()
                        < orderLineItemsDtoList.get(i).getQuantity())
                .forEachOrdered(i -> unavailabeSkuCodeList.add(quantityResponseList.get(i).getSkuCode()));

        if (!unavailabeSkuCodeList.isEmpty()) {
            return ResponseEntity.badRequest().body(messageSource.getMessage(
                    "error.not.in.stock", null, LocaleContextHolder.getLocale())
            + unavailabeSkuCodeList);
        }

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .userId(userId)
                .orderLineItemsList(orderLineItemsMapper.convertToEntityList(
                        orderLineItemsDtoList
                ))
                .build();

        //set bidirectional relationship fields
        order.getOrderLineItemsList().forEach(orderLineItems -> orderLineItems.setOrder(order));

        orderRepository.save(order);
        // If order saved update Inventory with new decremented quantity values
        List<InventoryRequest> inventoryRequestList = IntStream.range(0, orderLineItemsDtoList.size())
                .mapToObj(i -> InventoryRequest.builder()
                        .skuCode(orderLineItemsDtoList.get(i).getSkuCode())
                        .quantity(quantityResponseList.get(i).getQuantity() - orderLineItemsDtoList.get(i).getQuantity())
                        .build())
                .collect(Collectors.toList());

        inventoryFeignClient.updateInventory(inventoryRequestList);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageSource.getMessage(
                "success.order.places", null, LocaleContextHolder.getLocale()));
    }

    public List<OrderResponse> getOrderListByUserId(String userId) {
        List<OrderResponse> orderResponseList = new ArrayList<>();
        orderRepository.getOrderListByUserId(userId).forEach(order -> orderResponseList.add(
            OrderResponse.builder()
                    .orderNumber(order.getOrderNumber())
                    .orderLineItemsDtoList(orderLineItemsMapper.convertToDtoList(
                            order.getOrderLineItemsList())
                    ).build()
        ));

        return orderResponseList;
    }
}
