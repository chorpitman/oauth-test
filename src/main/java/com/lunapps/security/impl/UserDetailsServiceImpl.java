package com.lunapps.security.impl;

import com.lunapps.exception.user.UserRegistrationException;
import com.lunapps.models.Authority;
import com.lunapps.models.User;
import com.lunapps.models.UserRole;
import com.lunapps.repository.UserRepository;
import com.lunapps.security.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.text.MessageFormat.format;

@Service
public class UserDetailsServiceImpl implements Authentication {
    private static final String DOES_NOT_EXIST = "40401";
    private static final String HAS_TO_CONFIRM_REG = "40402";
    private static final String WRONG_PASSWORD = "40403";
    private static final String PASSWORDS_ARE_NOT_EQUALS = "40404";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userEmail);
        if (Objects.isNull(user)) {
            throw new UserRegistrationException(format("user with email: {0} does not exist", userEmail), DOES_NOT_EXIST);
        }

        if (user.getIsUserAccountActive() == Boolean.FALSE) {
            throw new UserRegistrationException(format("user with email: {0} has to confirm registration", userEmail), HAS_TO_CONFIRM_REG);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        Set<Authority> authorities = user.getAuthorities();

        for (Authority authority : authorities) {
            UserRole userRole = authority.getUserRole();
            grantedAuthorities.add(new SimpleGrantedAuthority(userRole.name()));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    // Perform the security
    @Override
    public void authCheck(String principal, String credentials) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials);
        final org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public String getCurrentAuthUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
