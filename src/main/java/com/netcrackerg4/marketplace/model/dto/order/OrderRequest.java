package com.netcrackerg4.marketplace.model.dto.order;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    @Pattern(regexp = ValidationConstants.PHONE_PATTERN, message = ValidationDefaultMessage.WRONG_FORMAT_PHONE_NUMBER)
    private String phoneNumber;
    @Future(message = "address must be specified to be in future")
    private LocalDateTime deliverySlot;
    @NotNull
    private AddressDto address;
    @Nullable
    private String comment;
    @NotEmpty(message = "no order without products possible")
    private List<OrderItemRequest> products;
}
