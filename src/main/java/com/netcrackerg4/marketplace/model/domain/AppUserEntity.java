package com.netcrackerg4.marketplace.model.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netcrackerg4.marketplace.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AppUserEntity implements UserDetails { // not exactly an Entity
    private final String userId;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final UserStatus status;
    private final int roleId;
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
