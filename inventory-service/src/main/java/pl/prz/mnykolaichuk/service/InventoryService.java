package pl.prz.mnykolaichuk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.prz.mnykolaichuk.model.dto.request.DecrementQuantityRequest;
import pl.prz.mnykolaichuk.model.dto.response.IsInStockResponse;
import pl.prz.mnykolaichuk.model.entity.Inventory;
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

    public String decrementQuantity (List<DecrementQuantityRequest> decrementQuantityRequestList) {

        decrementQuantityRequestList.forEach(request -> {
            String skuCode = request.getSkuCode();
            Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
            if (inventory != null) {
                inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
                inventoryRepository.save(inventory);
            }
        });

        return "New quantities set";
    }
}
