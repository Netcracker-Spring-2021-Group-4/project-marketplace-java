package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.AppUserEntity;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;

public interface IUserDao extends AbstractDAO<AppUserEntity, String> {
    List<GrantedAuthority> getAuthorities(int roleId);

    void setStatus(String email, UserStatus status);

    Optional<Integer> findStatusIdByStatusName(String name);
}
