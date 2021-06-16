package com.netcrackerg4.marketplace.util.mappers;

import com.netcrackerg4.marketplace.model.domain.product.DiscountEntity;
import com.netcrackerg4.marketplace.model.dto.product.DiscountDto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class DiscountEntity_Dao {
    public DiscountEntity toDiscountEntity(DiscountDto discountDto) {
        return DiscountEntity.builder()
                .offeredPrice(discountDto.getOfferedPrice())
                .startsAt(map(discountDto.getStartsAt()))
                .endsAt(map(discountDto.getEndsAt()))
                .build();
    }

    private Timestamp map(LocalDateTime value) {
        return Timestamp.from(value.toInstant(ZoneOffset.ofHours(3)));
    }
}
