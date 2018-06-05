package com.lunapps.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtTokenUtil implements Serializable {
//todo remove

    private static final long serialVersionUID = -3301605591108950415L;

//    static final String CLAIM_KEY_USERNAME = "sub";
//    static final String CLAIM_KEY_AUDIENCE = "aud";
//    static final String CLAIM_KEY_CREATED = "iat";
//    static final String CLAIM_KEY_EXPIRED = "exp";

    static final String AUDIENCE_UNKNOWN = "unknown";
    static final String AUDIENCE_WEB = "web";
    static final String AUDIENCE_MOBILE = "mobile";
    static final String AUDIENCE_TABLET = "tablet";

    @Autowired
    private DefaultClock timeProvider;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.token.expiration.time}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    private Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    //todo think about save into db
    private String getIdFromAccessToken(String token) {
        String tokenId;
        try {
            final Claims claims = getClaimsFromToken(token);
            tokenId = claims.getId();
        } catch (Exception e) {
            tokenId = null;
        }
        return tokenId;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(timeProvider.now());
    }

    private Boolean isTokenNonExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return timeProvider.now().before(expiration);
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

//    public String generateToken(UserDetails userDetails, Device device) {
//        Map<String, Object> claims = new HashMap<>();
//
//        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
//        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
//
//        final Date createdDate = timeProvider.now();
//        claims.put(CLAIM_KEY_CREATED, createdDate);
//
//        return doGenerateToken(claims);
//    }

//    private String doGenerateToken(Map<String, Object> claims) {
//        final Date createdDate = (Date) claims.get(CLAIM_KEY_CREATED);
//        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
//
//        System.out.println("doGenerateToken " + createdDate);
//
//        String generatedToken = Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(expirationDate)
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//        return generatedToken;
//    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

//    public String refreshToken(String token) {
//        String refreshedToken;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            claims.put(CLAIM_KEY_CREATED, timeProvider.now());
//            refreshedToken = doGenerateToken(claims);
//        } catch (Exception e) {
//            refreshedToken = null;
//        }
//        return refreshedToken;
//    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String usernameFromToken = getUsernameFromToken(token);
        return (Objects.equals(usernameFromToken, user.getUsername()) && isTokenNonExpired(token)
//                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
        );
    }
}