package com.netcrackerg4.marketplace.model.dto.user;

import com.netcrackerg4.marketplace.constants.ValidationConstants;
import com.netcrackerg4.marketplace.constants.ValidationDefaultMessage;
import com.netcrackerg4.marketplace.model.enums.ProductCategory;
import com.netcrackerg4.marketplace.validation.ProductCategoryTypeSubset;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.Date;

@Data
public class NewProductDto {

    @NotNull
    @Pattern(
            regexp = ValidationConstants.NAME_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_FIRST_NAME
    )
    private String productName;

    private String description;

    @Min(value=1,message="The price must be greater than 0")
    private int price;

    @Min(value=0,message="Must be equal or greater than 0")
    private int inStock;

    @Min(value=0,message="must be equal or greater than 0")
    private int reserved;

    @NotNull
    @Pattern(
            regexp = ValidationConstants.DATE_PATTERN,
            message = ValidationDefaultMessage.WRONG_FORMAT_FIRST_NAME
    )
    private Date date;

    @NotNull
    @ProductCategoryTypeSubset(anyOf = {ProductCategory.RED_WINE, ProductCategory.ROSE_WINE,
            ProductCategory.WHITE_WINE,ProductCategory.DESSERT_WINE, ProductCategory.BLUE_CHEESE,ProductCategory.HARD_CHEESE,
            ProductCategory.PASTA_FILATA_CHEESE,ProductCategory.PROCESSED_CHEESE,ProductCategory.SEMI_HARD_CHEESE,
            ProductCategory.SEMI_SOFT_CHEESE,ProductCategory. SOFT_FRESH_CHEESE,ProductCategory.SOFT_RIPENED_CHEESE})
    private ProductCategory productCategory;


}
