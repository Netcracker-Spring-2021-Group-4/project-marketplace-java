package com.netcrackerg4.marketplace.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    private UUID addressId;
    private String city;
    private String street;
    private String building;
    @Nullable
    private Integer flat;
    private UUID customerId;
}
