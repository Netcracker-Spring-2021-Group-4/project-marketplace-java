package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.domain.ProductEntity;

import java.net.URL;
import java.util.UUID;

public interface IProductDao extends AbstractCrudRepository<ProductEntity, UUID> {
     void updatePicture(UUID key, URL url);

}
