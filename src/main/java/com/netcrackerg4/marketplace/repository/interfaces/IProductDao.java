package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.domain.AppProductEntity;

import java.util.List;
import java.util.UUID;

public interface IProductDao extends AbstractCrudRepository<AppProductEntity, UUID>{

  List<AppProductEntity> getAllProducts();

  void deactivateProduct(UUID productId);
}
