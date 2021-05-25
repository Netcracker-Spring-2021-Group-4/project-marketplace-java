package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.AppUserEntity;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserDao extends AbstractCrudRepository<AppUserEntity, UUID> {
    Optional<AppUserEntity> findByEmail(String email);

    List<GrantedAuthority> getAuthorities(int roleId);

    void updateStatus(String email, UserStatus status);

    void updatePassword(String email, String password);

    Integer findStatusIdByStatusName(String name);

    Integer findRoleIdByRoleName(String roleName);

}
