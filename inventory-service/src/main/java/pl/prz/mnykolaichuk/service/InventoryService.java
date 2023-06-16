package pl.prz.mnykolaichuk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.prz.mnykolaichuk.model.dto.response.IsInStockResponse;
import pl.prz.mnykolaichuk.repository.InventoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<IsInStockResponse> isInStock(List<String> skuCodeList) {
        //  inventoryRepository List<InventoryEntity>

        return inventoryRepository.findBySkuCodeIn(skuCodeList).stream()
                .map(inventory ->
                        IsInStockResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
