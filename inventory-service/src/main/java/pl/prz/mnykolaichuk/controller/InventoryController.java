package pl.prz.mnykolaichuk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.prz.mnykolaichuk.model.dto.request.DecrementQuantityRequest;
import pl.prz.mnykolaichuk.model.dto.response.IsInStockResponse;
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
    @PostMapping("/quantity")
    @ResponseStatus(HttpStatus.OK)
    public String decrementQuantity(@RequestBody List<DecrementQuantityRequest> decrementQuantityRequestList) {
        return inventoryService.decrementQuantity(decrementQuantityRequestList);
    }
}
