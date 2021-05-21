package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.dto.product.ProductDto;

import java.util.UUID;

public interface IProductDao extends AbstractCrudRepository<ProductDto, UUID> {


}
