package com.netcrackerg4.marketplace.util.mappers;

import com.netcrackerg4.marketplace.model.domain.AddressEntity;
import com.netcrackerg4.marketplace.model.dto.order.AddressDto;
import org.springframework.beans.BeanUtils;

public class AddressMapper {
    public static AddressDto entityToDto(AddressEntity addressEntity) {
        AddressDto addressDto = new AddressDto();
        BeanUtils.copyProperties(addressEntity, addressDto);
        return addressDto;
    }
}
