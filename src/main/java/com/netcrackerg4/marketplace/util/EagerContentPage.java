package com.netcrackerg4.marketplace.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EagerContentPage<T> {
    private List<T> content;
    private int numPages;
    private int fullPageSize;
}
