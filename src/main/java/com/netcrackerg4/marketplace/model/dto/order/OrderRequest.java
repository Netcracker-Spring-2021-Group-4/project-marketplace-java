package com.netcrackerg4.marketplace.model.dto.order;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    @NotNull
    @Pattern(regexp = ValidationConstants.PHONE_PATTERN, message = ValidationDefaultMessage.WRONG_FORMAT_PHONE_NUMBER)
    private String phoneNumber;
    @NotNull
    @Future(message = "delivery slot must be specified to be in future")
    private LocalDateTime deliverySlot;
    @NotNull
    private AddressDto address;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Nullable
    private String comment;
    @Valid
    @UniqueElements
    @NotEmpty(message = "no order without products possible")
    private List<OrderItemRequest> products;
}
