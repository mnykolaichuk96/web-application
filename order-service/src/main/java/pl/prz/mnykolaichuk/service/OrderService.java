package pl.prz.mnykolaichuk.service;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.prz.mnykolaichuk.model.dto.OrderRequest;
import pl.prz.mnykolaichuk.model.dto.request.DecrementQuantityRequest;
import pl.prz.mnykolaichuk.model.dto.response.IsInStockResponse;
import pl.prz.mnykolaichuk.model.dto.response.OrderResponse;
import pl.prz.mnykolaichuk.model.entity.Order;
import pl.prz.mnykolaichuk.model.entity.OrderLineItems;
import pl.prz.mnykolaichuk.model.mapper.OrderLineItemsMapper;
import pl.prz.mnykolaichuk.repository.OrderLineItemsRepository;
import pl.prz.mnykolaichuk.repository.OrderRepository;
import pl.prz.mnykolaichuk.service.rest.client.InventoryFeignClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private final Tracer tracer;
    private final OrderLineItemsMapper orderLineItemsMapper = new OrderLineItemsMapper();
    private final InventoryFeignClient inventoryFeignClient;
    private final OrderRepository orderRepository;
    private final OrderLineItemsRepository orderLineItemsRepository;
    public String placeOrder(OrderRequest orderRequest, String userId){
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .userId(userId)
                .orderLineItemsList(orderLineItemsMapper.convertToEntityList(
                        orderRequest.getOrderLineItemsDtoList()
                ))
                .build();

        order.getOrderLineItemsList().forEach(orderLineItems -> orderLineItems.setOrder(order));

        List<String> skuCodeList = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // For tracing any peas of code in zipkin, create span
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
            List<IsInStockResponse> isInStockResponseList = inventoryFeignClient.isInStock(skuCodeList);

            boolean allProductsInStock = isInStockResponseList.stream()
                    .allMatch(IsInStockResponse::getIsInStock);
            if (allProductsInStock) {
                orderRepository.save(order);
                List<DecrementQuantityRequest> decrementQuantityRequestList = new ArrayList<>();
                order.getOrderLineItemsList().forEach(orderLineItems -> {
                    decrementQuantityRequestList.add(
                        DecrementQuantityRequest.builder()
                                .skuCode(orderLineItems.getSkuCode())
                                .quantity(orderLineItems.getQuantity())
                                .build()
                    );
                });
                inventoryFeignClient.decrementQuantity(decrementQuantityRequestList);

                return "Order Places Successfully";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        } finally {
            inventoryServiceLookup.end();
        }
    }

    public List<OrderResponse> getOrderListByUserId(String userId) {
        List<OrderResponse> orderResponseList = new ArrayList<>();
        orderRepository.getOrderListByUserId(userId).stream().forEach(order -> {
            orderResponseList.add(
                OrderResponse.builder()
                        .orderNumber(order.getOrderNumber())
                        .orderLineItemsDtoList(orderLineItemsMapper.convertToDtoList(
                                order.getOrderLineItemsList())
                        ).build()
            );
        });

        return orderResponseList;
    }
}
