package pl.prz.mnykolaichuk.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IsInStockResponse {
    private String skuCode;
    private Boolean isInStock;
}

