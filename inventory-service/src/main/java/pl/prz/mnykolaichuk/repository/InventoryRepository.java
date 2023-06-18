package pl.prz.mnykolaichuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.prz.mnykolaichuk.model.entity.Inventory;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCodeList);
    Inventory findBySkuCode(String skuCode);
}
