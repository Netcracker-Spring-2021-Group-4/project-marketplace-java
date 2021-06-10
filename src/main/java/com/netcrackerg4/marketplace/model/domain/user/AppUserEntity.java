package com.netcrackerg4.marketplace.model.domain.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netcrackerg4.marketplace.model.enums.UserRole;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AppUserEntity implements UserDetails { // not exactly an Entity
    private final UUID userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @Nullable
    private final String phoneNumber;
    private final UserStatus status;
    private final UserRole role;
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // TODO ask when it's used
    @Override
    public boolean isAccountNonLocked() {
//         return status != Status.TERMINATED || status != Status.UNCONFIRMED;
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // TODO ask when it's used
    @Override
    public boolean isEnabled() {
//        return status == Status.ACTIVE;
        return true;
    }
}
