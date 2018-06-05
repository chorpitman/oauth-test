package com.lunapps.security;

import com.lunapps.components.JwtHeaderTokenExtractor;
import com.lunapps.components.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHeaderTokenExtractor tokenExtractor;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String payloadToken = request.getHeader(this.tokenHeader);
        if (payloadToken != null) {
            String authToken = tokenExtractor.extract(payloadToken);

            if (StringUtils.isNotBlank(authToken)) {

                String userEmail = jwtTokenUtil.getUsernameFromToken(authToken);
                LOGGER.info("checking authentication for user " + userEmail);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // It is not compelling necessary to load the use details from the database. You could also store the information
                    // in the token and read it from it. It's up to you ;)
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                    // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                    // the database compellingly. Again it's up to you ;)
                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        LOGGER.info("authenticated user " + userEmail + ", setting security context");

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

        }
        chain.doFilter(request, response);
    }
}