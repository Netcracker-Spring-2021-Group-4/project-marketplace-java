package com.netcrackerg4.marketplace.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {
    private List<T> content;
    private int totalItems;
}
