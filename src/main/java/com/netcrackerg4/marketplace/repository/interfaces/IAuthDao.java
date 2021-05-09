package com.netcrackerg4.marketplace.repository.interfaces;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;

public interface IAuthDao {
    Optional<Integer> getRoleIdByName(String roleName);

    List<GrantedAuthority> getPermissionsByRoleId(int roleId);
}
