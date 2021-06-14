package com.netcrackerg4.marketplace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentErrorWrapper<T> {
    private T content;
    private String error;
}
