package com.netcrackerg4.marketplace.service.interfaces;

import java.util.UUID;

public interface ICategoryService {
    String findNameById(int id);
    String getCategoryNameByProductId(UUID productId);
}
