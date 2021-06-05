package com.netcrackerg4.marketplace.model.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FilterInfo {

    List<CategoryResponse> categories;
    int maxPrice;


    @Data
    @AllArgsConstructor
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class CategoryResponse {
        private int categoryId;
        private String productCategoryName;
        private int productsInCategory;
    }

}
