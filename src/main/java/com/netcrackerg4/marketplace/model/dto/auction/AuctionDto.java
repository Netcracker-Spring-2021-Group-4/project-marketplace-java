package com.netcrackerg4.marketplace.model.dto.auction;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AuctionDto {
    @Future(message = ValidationDefaultMessage.WRONG_FORMAT_AUCTION_STARTS_AT)
    private LocalDateTime startsAt;
    @Min(value= 100_00, message = ValidationDefaultMessage.WRONG_FORMAT_AUCTION_START_PRICE)
    @Max(value = 120_567_00, message = ValidationDefaultMessage.WRONG_FORMAT_AUCTION_START_PRICE_MAX)
    private int startPrice;
    @Min(value= 3, message = ValidationDefaultMessage.WRONG_FORMAT_AUCTION_PRODUCT_QUANTITY)
    private int productQuantity;
    @NotNull
    private UUID productId;
    @Min(value=1)
    private int typeId;
    @Valid
    @Delegate
    @JsonDeserialize(using = JsonDetailsDeserializer.class)
    private JsonDetails jsonDetails;
}
