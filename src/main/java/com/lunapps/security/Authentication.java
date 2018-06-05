package com.lunapps.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by olegchorpita on 8/8/17.
 */
public interface Authentication extends UserDetailsService {
    UserDetails loadUserByUsername(final String userEmail);

    void authCheck(String principal, String credentials);

    String getCurrentAuthUser();
}