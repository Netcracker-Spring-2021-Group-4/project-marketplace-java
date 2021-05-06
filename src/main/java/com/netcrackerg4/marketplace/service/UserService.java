package com.netcrackerg4.marketplace.service;

import com.netcrackerg4.marketplace.repository.interfaces.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = userDAO.findByIdx(s)
                .orElseThrow(() -> new UsernameNotFoundException("User with such email not found"));
        user.setAuthorities(userDAO.getAuthorities(user.getRoleId()));
        // TODO get status
        return user;
    }
}
