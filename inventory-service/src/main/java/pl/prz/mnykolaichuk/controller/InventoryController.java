package pl.prz.mnykolaichuk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.prz.mnykolaichuk.model.dto.request.InventoryRequest;
import pl.prz.mnykolaichuk.model.dto.response.IsInStockResponse;
import pl.prz.mnykolaichuk.model.dto.response.QuantityResponse;
import pl.prz.mnykolaichuk.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/stock")
    @ResponseStatus(HttpStatus.OK)
    public List<IsInStockResponse> isInStock(@RequestParam List<String> skuCodeList) {
        return inventoryService.isInStock(skuCodeList);
    }
    @GetMapping("/quantity")
    @ResponseStatus(HttpStatus.OK)
    public List<QuantityResponse> getQuantity(@RequestParam List<String> skuCodeList) {
        return inventoryService.getQuantity(skuCodeList);
    }

    @PostMapping("/add")
    public ResponseEntity<String> placeInventory(@RequestBody List<InventoryRequest> inventoryRequestList) {
        return
                inventoryService.placeInventory(inventoryRequestList);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateInventory(@RequestBody List<InventoryRequest> inventoryRequestList) {
        return
                inventoryService.placeInventory(inventoryRequestList);
    }
}
