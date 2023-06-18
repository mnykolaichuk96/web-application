package pl.prz.mnykolaichuk.model.mapper;

import org.springframework.beans.BeanUtils;
import pl.prz.mnykolaichuk.model.dto.OrderLineItemsDto;
import pl.prz.mnykolaichuk.model.entity.OrderLineItems;

public class OrderLineItemsMapper extends BaseMapper<OrderLineItems, OrderLineItemsDto> {
    @Override
    public OrderLineItems convertToEntity(OrderLineItemsDto dto, Object... args) {
        OrderLineItems entity = new OrderLineItems();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);  // next argument -- ignored filed optinal
        }
        return entity;
    }

    @Override
    public OrderLineItemsDto convertToDto(OrderLineItems entity, Object... args) {
        OrderLineItemsDto dto = new OrderLineItemsDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
