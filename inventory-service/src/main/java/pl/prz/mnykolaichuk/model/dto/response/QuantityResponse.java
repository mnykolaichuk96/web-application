package pl.prz.mnykolaichuk.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class QuantityResponse {
    private String skuCode;
    private Integer quantity;
}
