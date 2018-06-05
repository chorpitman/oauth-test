package com.lunapps.config.oauth;

import com.lunapps.security.impl.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * Created by olegchorpita on 8/28/17.
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    //    private static final String REALM = "MY_OAUTH_REALM";
    private static final String BETOSHOOK_RESOURCE = "http://betoshook";

    private static final String SECRET_SIGN = "123";

    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24; //24h
    private static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 48; //48h

    //provide custom fields in token
    @Bean(name = "customTokenEnhancer")
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    //for client (in memory)
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .allowFormAuthenticationForClients()
                // we're allowing access to the token only for clients with 'ROLE_TRUSTED_CLIENT' authority
                .tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception { // @formatter:off
        clients
                .inMemory()
                //ios client
                .withClient("ios-my-trusted-client").secret("secret")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                .scopes("read", "write")
                .resourceIds(BETOSHOOK_RESOURCE)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)//Access token is only valid for 2 minutes.
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS)//Refresh token is only valid for 10 minutes.
                //web client
                .and()
                //ios client
                .withClient("web-my-trusted-client").secret("secret")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                .scopes("read", "write")
                .resourceIds(BETOSHOOK_RESOURCE)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)//Access token is only valid for 2 minutes.
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);//Refresh token is only valid for 10 minutes.
    }

    //todo need fix
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        //need for activate jwt token
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), accessTokenConverter()));

        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    //todo make endpoint for fb call back url, redirect url ->spring social oauth2
    // JWT
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setTokenEnhancer(tokenEnhancer());
        tokenServices.setSupportRefreshToken(true);
        return tokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SECRET_SIGN);
        return converter;
    }
}