package com.netcrackerg4.marketplace.model.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class RecommendationEntity {
    private UUID productA;
    private UUID productB;
    private double lift;

}
