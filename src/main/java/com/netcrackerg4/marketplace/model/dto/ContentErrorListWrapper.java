package com.netcrackerg4.marketplace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentErrorListWrapper<T> {
    private T content;
    private List<String> errors;
}
