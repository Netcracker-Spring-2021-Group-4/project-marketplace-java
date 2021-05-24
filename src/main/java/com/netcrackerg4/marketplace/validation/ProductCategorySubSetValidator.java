package com.netcrackerg4.marketplace.validation;

import com.netcrackerg4.marketplace.model.enums.ProductCategory;
import com.netcrackerg4.marketplace.model.enums.UserStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;



public class ProductCategorySubSetValidator implements ConstraintValidator<ProductCategoryTypeSubset, ProductCategory> {
    private ProductCategory[] subset;

    @Override
    public void initialize(ProductCategoryTypeSubset constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(ProductCategory productCategory, ConstraintValidatorContext constraintValidatorContext) {
        return productCategory == null || Arrays.asList(subset).contains(productCategory);
    }
}

