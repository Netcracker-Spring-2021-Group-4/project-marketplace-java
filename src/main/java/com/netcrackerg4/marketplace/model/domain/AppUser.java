package com.netcrackerg4.marketplace.model.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netcrackerg4.marketplace.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AppUser implements UserDetails {
    private final String email;
    private final String password;
    private final int roleId;
    private Set<GrantedAuthority> authorities;
    private Status status;

    public AppUser( String email, String password, int roleId) {
        // TODO change
        this(email, password, roleId, null, Status.TERMINATED);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
