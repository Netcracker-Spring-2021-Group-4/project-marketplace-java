package com.netcrackerg4.marketplace.model.dto.product;


import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ProductSearchFilter {
    @Nullable
    private String sortOption;
    @NotNull
    private List<Integer> categoryIds;
    @Nullable
    @Size(min = 2)
    private String nameQuery;
    @Nullable
    @Min(0)
    private Double minPrice;
    @Min(1)
    @Nullable
    private Double maxPrice;
}
