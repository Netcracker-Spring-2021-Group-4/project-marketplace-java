package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.AppUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public interface UserDAO extends AbstractDAO<AppUser, String>{
    Set<GrantedAuthority> getAuthorities(int roleId);
}
