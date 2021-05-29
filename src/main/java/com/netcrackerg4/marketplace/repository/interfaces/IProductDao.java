package com.netcrackerg4.marketplace.repository.interfaces;


import com.netcrackerg4.marketplace.model.domain.AppProductEntity;

import java.net.URL;
import java.util.UUID;

public interface IProductDao extends AbstractCrudRepository<AppProductEntity, UUID>{
     void updatePicture(UUID key, URL url);
}
