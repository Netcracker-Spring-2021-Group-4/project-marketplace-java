package com.netcrackerg4.marketplace.model.dto.order;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import com.netcrackerg4.marketplace.model.dto.timestamp.StatusTimestampDto;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class OrderRequest {
    // todo: use phone number pattern
    @Pattern(regexp = ValidationConstants.PHONE_PATTERN, message = ValidationDefaultMessage.WRONG_FORMAT_PHONE_NUMBER)
    private String phoneNumber;
    @NotNull(message = "timeslot for delivery must be specified")
    private StatusTimestampDto deliverySlot;
    @NotBlank(message = "address must be specified")
    private String address;
    @Nullable
    private String comment;
    @NotEmpty(message = "no order without products possible")
    private List<OrderedProductRequest> products;
}
