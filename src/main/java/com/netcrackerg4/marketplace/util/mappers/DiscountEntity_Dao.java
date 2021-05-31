package com.netcrackerg4.marketplace.util.mappers;

import com.netcrackerg4.marketplace.model.domain.DiscountEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import org.mapstruct.Mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface DiscountEntity_Dao {
    DiscountEntity toDiscountEntity(DiscountDto discountDto);

    default Timestamp map(LocalDateTime value) {
        return Timestamp.from(value.toInstant(ZoneOffset.UTC));
    }
}
