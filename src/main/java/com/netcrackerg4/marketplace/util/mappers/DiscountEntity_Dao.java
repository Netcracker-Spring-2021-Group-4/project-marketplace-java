package com.netcrackerg4.marketplace.util.mappers;

import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface DiscountEntity_Dao {
    @Mappings({
            @Mapping(target = "productId", source = "targetProductId")
    })
    DiscountEntity toDiscountEntity(DiscountDto discountDto);

    default Timestamp map(LocalDateTime value) {
        return Timestamp.from(value.toInstant(ZoneOffset.UTC));
    }
}
