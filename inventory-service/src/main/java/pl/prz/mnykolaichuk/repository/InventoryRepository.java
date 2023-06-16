package pl.prz.mnykolaichuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.prz.mnykolaichuk.model.entity.InventoryEntity;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    List<InventoryEntity> findBySkuCodeIn(List<String> skuCodeList);
}
