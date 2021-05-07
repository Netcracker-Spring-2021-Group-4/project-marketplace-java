package com.netcrackerg4.marketplace.model.domain;

import lombok.Data;

// review: isn't it a DTO, if so there is a dedicated package

@Data
public class UsernamePasswordDataObject {
    private String username;
    private String password;
}
