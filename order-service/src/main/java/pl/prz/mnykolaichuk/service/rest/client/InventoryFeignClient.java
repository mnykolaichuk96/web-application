package pl.prz.mnykolaichuk.service.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.prz.mnykolaichuk.configuration.FeignClientConfiguration;
import pl.prz.mnykolaichuk.model.dto.request.InventoryRequest;
import pl.prz.mnykolaichuk.model.dto.response.IsInStockResponse;
import pl.prz.mnykolaichuk.model.dto.response.QuantityResponse;

import java.util.List;

@FeignClient(value = "inventory-service", configuration = FeignClientConfiguration.class)
public interface InventoryFeignClient {
    @GetMapping(path = "/api/inventory/stock?skuCodeList={skuCodeList}")
    List<IsInStockResponse> isInStock(@PathVariable("skuCodeList")List<String> skuCodeList);
    @GetMapping(path = "/api/inventory/quantity?skuCodeList={skuCodeList}")
    List<QuantityResponse> getQuantity(@PathVariable("skuCodeList")List<String> skuCodeList);

    @PostMapping(path = "/api/inventory/update")
    String updateInventory(@RequestBody List<InventoryRequest> inventoryRequestList);
}
