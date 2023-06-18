package pl.prz.mnykolaichuk.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.prz.mnykolaichuk.model.dto.OrderLineItemsDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private String orderNumber;
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
