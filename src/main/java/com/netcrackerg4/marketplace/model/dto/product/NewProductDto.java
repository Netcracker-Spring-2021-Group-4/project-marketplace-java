package com.netcrackerg4.marketplace.model.dto.product;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class NewProductDto {

    @NotNull
    @Pattern(
            regexp = ValidationConstants.DESCRIPTIVE_NAME_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_NAMING
    )
    private String productName;

    private String description;

    @Min(value=5,message="The price must be equal or greater than 5")
    @Max(value=2359800,message="The price must be equal or lower than 2359800")
    private int price;

    @Min(value=0,message="Must be equal or greater than 0")
    private int inStock;

    @Min(value=0,message="must be equal or greater than 0")
    private int reserved;

    @NotNull
    private int categoryId;


}
