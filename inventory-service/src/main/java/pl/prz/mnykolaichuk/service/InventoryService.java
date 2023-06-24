package pl.prz.mnykolaichuk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.prz.mnykolaichuk.model.dto.InventoryDto;
import pl.prz.mnykolaichuk.model.dto.request.InventoryRequest;
import pl.prz.mnykolaichuk.model.dto.response.IsInStockResponse;
import pl.prz.mnykolaichuk.model.dto.response.QuantityResponse;
import pl.prz.mnykolaichuk.model.mapper.InventoryMapper;
import pl.prz.mnykolaichuk.repository.InventoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper = new InventoryMapper();
    private final MessageSource messageSource;

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



    public ResponseEntity<String> placeInventory(List<InventoryRequest> inventoryRequestList) {
        // If one element called badRequest - doesn't save anything
        List<InventoryDto> inventoryDtoList = new ArrayList<>();
        if (inventoryRequestList != null && !inventoryRequestList.isEmpty()) {
            for (InventoryRequest inventoryRequest : inventoryRequestList) {
                if (inventoryRequest.getQuantity() >= 0) {
                    InventoryDto inventoryDto = InventoryDto.builder()
                            .skuCode(inventoryRequest.getSkuCode())
                            .quantity(inventoryRequest.getQuantity())
                            .build();
                    // If skuCode already in db update quantity
                    if (!isSkuCodeUnique(inventoryRequest.getSkuCode())) {
                        inventoryDto.setId(
                                inventoryRepository.findBySkuCode(inventoryDto.getSkuCode()).getId()
                        );
                    }
                    inventoryDtoList.add(inventoryDto);
                } else {
                    return ResponseEntity.badRequest().body(
                            messageSource.getMessage(
                                    "error.quantity.negative", null, LocaleContextHolder.getLocale()
                            )
                    );
                }
            }
        } else {
            return ResponseEntity.badRequest().body(
                    messageSource.getMessage("error.request.empty", null, LocaleContextHolder.getLocale())
            );
        }

        //saving or updating all inventories
        for(InventoryDto inventoryDto: inventoryDtoList) {
            inventoryRepository.save(inventoryMapper.convertToEntity(inventoryDto));
        }
        return ResponseEntity.ok().body(
                messageSource.getMessage(
                        "success.inventory.places", null, LocaleContextHolder.getLocale())
        );
    }

    private boolean isSkuCodeUnique(String skuCode){
        return inventoryRepository.findBySkuCode(skuCode) == null;
    }

    public List<QuantityResponse> getQuantity(List<String> skuCodeList) {
        return inventoryRepository.findBySkuCodeIn(skuCodeList).stream()
                .map(inventory ->
                        QuantityResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .quantity(inventory.getQuantity())
                                .build()
                ).toList();
    }
}
