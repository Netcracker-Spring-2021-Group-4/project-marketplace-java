package com.netcrackerg4.marketplace.repository.interfaces;

import java.util.Optional;

public interface ICategoryDao {
    Optional<String> findNameById(int id);
}
