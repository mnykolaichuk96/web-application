package pl.prz.mnykolaichuk.model.mapper;

import org.springframework.beans.BeanUtils;
import pl.prz.mnykolaichuk.model.dto.InventoryDto;
import pl.prz.mnykolaichuk.model.entity.Inventory;

public class InventoryMapper extends BaseMapper<Inventory, InventoryDto> {
    @Override
    public Inventory convertToEntity(InventoryDto dto, Object... args) {
        Inventory entity = new Inventory();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);  // next argument -- ignored filed optinal
        }
        return entity;
    }

    @Override
    public InventoryDto convertToDto(Inventory entity, Object... args) {
        InventoryDto dto = new InventoryDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
