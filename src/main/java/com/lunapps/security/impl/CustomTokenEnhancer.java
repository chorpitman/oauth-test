package com.lunapps.security.impl;

import com.lunapps.controllers.dto.UserProfileTokenDto;
import com.lunapps.convertor.UserDtoConverter;
import com.lunapps.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by i4790k on 11.09.2017.
 */
@Service
public class CustomTokenEnhancer implements TokenEnhancer {
    @Autowired
    UserDtoConverter converter;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String authenticationName = authentication.getName();
        com.lunapps.models.User user = userRepository.findByEmail(authenticationName);
        UserProfileTokenDto tokenProfileDto = converter.convertToTokenProfileDto(user);

        final Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("user", tokenProfileDto);
//        todo think about customize additional fields for jwt
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}

