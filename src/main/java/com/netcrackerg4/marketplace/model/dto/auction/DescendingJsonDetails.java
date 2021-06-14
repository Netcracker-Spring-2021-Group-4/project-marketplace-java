package com.netcrackerg4.marketplace.model.dto.auction;

import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class DescendingJsonDetails implements JsonDetails {
    @Min(value = 5, message = ValidationDefaultMessage.WRONG_FORMAT_LOWERING_STEP)
    private int loweringStep;
    @Min(value = 60, message = ValidationDefaultMessage.WRONG_FORMAT_STEP_PERIOD)
    private int stepPeriod;
    @Min(value = 1, message = ValidationDefaultMessage.WRONG_FORMAT_NUM_STEPS)
    private int numSteps;
}
