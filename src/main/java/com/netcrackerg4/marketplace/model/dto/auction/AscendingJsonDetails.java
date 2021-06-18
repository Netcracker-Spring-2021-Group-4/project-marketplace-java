package com.netcrackerg4.marketplace.model.dto.auction;

import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class AscendingJsonDetails implements JsonDetails {
    @Min(value= 10, message = ValidationDefaultMessage.WRONG_FORMAT_TIME_TO_BID)
    private int timeToBid;
    @Min(value= 500, message = ValidationDefaultMessage.WRONG_FORMAT_MIN_RISE)
    private int minRise;
}
