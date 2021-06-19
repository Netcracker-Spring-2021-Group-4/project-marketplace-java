package com.netcrackerg4.marketplace.model.dto.product;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
public class DiscountDto {
    @Min(value = 1, message = "minimal price is '0.01'")
    @Max(value = 23598,message = "maximal price is '23598'")
    private int offeredPrice;
    @Future(message = "beginning of a new discount cannot be in a past")
    private LocalDateTime startsAt;
    @Future(message = "discount cannot be finished on creation")
    private LocalDateTime endsAt;
}
