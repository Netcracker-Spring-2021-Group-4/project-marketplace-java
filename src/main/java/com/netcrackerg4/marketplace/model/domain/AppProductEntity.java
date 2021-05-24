package com.netcrackerg4.marketplace.model.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netcrackerg4.marketplace.model.enums.ProductCategory;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class AppProductEntity {
    private final String productId;
    private String name;
    private String description;
    private String imageUrl;
    private int price;
    private int inStock;
    private int reserved;
    private Date availabilityDate;
    private Boolean isActive;
    private ProductCategory category;


}