package com.netcrackerg4.marketplace.model.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class AddressDto {
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotBlank
    private String building;
    @Nullable
    private Integer flat;
}
