package com.netcrackerg4.marketplace.model.dto.product;


import com.netcrackerg4.marketplace.model.enums.SortingOptions;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ProductSearchFilter {
    @Nullable
    private SortingOptions sortOption;
    @Nullable
    private List<Integer> categoryIds;
    @Nullable
    @Size(max = 20)
    private String nameQuery;
    @Min(0)
    private int minPrice;
    @Min(0)
    private int maxPrice;
}
