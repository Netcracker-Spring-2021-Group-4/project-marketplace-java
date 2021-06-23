package com.netcrackerg4.marketplace.service.implementations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class AprioriAlgorithmServiceTest {
    @Autowired
    AprioriAlgorithmService service ;

    @Test
    void algorithm() {
        System.out.println(service);
        service.algorithm();
    }
}