package com.netcrackerg4.marketplace.repository.interfaces;

import com.netcrackerg4.marketplace.model.domain.user.AppUserEntity;
import com.netcrackerg4.marketplace.model.dto.user.UserAdminView;
import com.netcrackerg4.marketplace.model.enums.UserRole;
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

    List<UserAdminView> findUsersByFilter(List<UserRole> targetRoles, List<UserStatus> targetStatuses, String firstName, String lastName, int pageSize, int page);

    int countFilteredUsers(List<UserRole> roles, List<UserStatus> statuses, String firstName, String lastName);
}
