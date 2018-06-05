package com.lunapps.services.impl;

import com.lunapps.components.JwtTokenUtil;
import com.lunapps.repository.UserRepository;
import com.lunapps.services.TokenService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;

/**
 * Created by olegchorpita on 8/2/17.
 */

@Service
public class TokenServiceImpl implements TokenService {
    private static final int byteLength = 20;
    private static final String UID = "uid";
    private static final String REF = "ref";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private AuthInfoRepository infoRepository;

    @Value("${jwt.refresh.token.expiration.time}")
    private Long refreshTokenExpirationTime;

    @Value("${jwt.access.token.expiration.time}")
    private Long accessTokenExpirationTime;

    @Value("${jwt.secret}")
    private String secretSignKey;

    @Value("${jwt.token.issuer}")
    private String issuer;

//    @Override
//    public String createJwtRefreshToken(UserDetails userDetails, Device device) {
//        if (StringUtils.isBlank(userDetails.getUsername())) {
//            throw new IllegalArgumentException("Cannot create JWT Token without username");
//        }
//
//        LocalDateTime currentTime = LocalDateTime.now();
//
//        //if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
//        //throw new IllegalArgumentException("User doesn't have any privileges");
//
//        //todo Vlad told me that i have to set user id in subject
//        String email = userDetails.getUsername();
//        User foundUser = userRepository.findByEmail(email);
//
//        String jti = new String(Base64.encodeBase64URLSafe((generateRandomBytes(byteLength))));
//
//        AuthInfo authInfo = new AuthInfo();
//        authInfo.setJwtJti(jti);
//        authInfo.setCreationDate(LocalDateTime.now());
//        authInfo.setExpiryDate(LocalDateTime.now().plusMinutes(60));
//
////        if (foundUser.getAuthInfo() != null) {
////            todo remove all refresh tokens
////            foundUser.setAuthInfo(null);
////            userRepository.save(foundUser);
////            infoRepository.deleteAll();
////        }
//
////        foundUser.setAuthInfo(authInfo);
//        userRepository.save(foundUser);
//
//        String foundUserId = foundUser.getId().toString();
//
//        Claims claims = Jwts.claims();
//        claims.put(UID, foundUserId);
//        claims.setId(jti);
//        claims.setIssuer(issuer);
//        claims.setAudience(jwtTokenUtil.generateAudience(device));
//        claims.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()));
//
//        //claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
//        String refreshToken = Jwts.builder()
//                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, secretSignKey)
//                .compact();
//
//        return refreshToken;
//    }
//
//    @Override
//    public String createJwtAccessToken(UserDetails userDetails, Device device) {
//        if (StringUtils.isBlank(userDetails.getUsername()))
//            throw new IllegalArgumentException("Cannot create JWT Token without username");
//
//        LocalDateTime currentTime = LocalDateTime.now();
//        String email = userDetails.getUsername();
//        User foundUser = userRepository.findByEmail(email);
////        String s = foundUser.getAuthInfo().getId().toString();
//
//        Claims claims = Jwts.claims();
//        claims.put(UID, foundUser.getId().toString());
////        claims.put(REF, s);
//        claims.setExpiration(Date.from(currentTime
//                .plusMinutes(accessTokenExpirationTime)
//                .atZone(ZoneId.systemDefault()).toInstant()));
//
//        //        claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
//
//        String accessToken = Jwts.builder()
//                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS512, secretSignKey)
//                .compact();
//
//        return accessToken;
//    }

    @Override
    public HashMap<String, String> generateMailToken(String userEmailToken, String userDbToken) {
        byte[] secretBytes = generateRandomBytes(byteLength);
        String urlTokenForUser = new String(Base64.encodeBase64URLSafe((secretBytes)));
        String tokenForDb = new String(Base64.encodeBase64(DigestUtils.sha512(urlTokenForUser)));

        HashMap<String, String> tokens = new HashMap<>();
        tokens.put(userEmailToken, urlTokenForUser);
        tokens.put(userDbToken, tokenForDb);

        return tokens;
    }

    @Override
    public byte[] generateRandomBytes(int length) {
        byte bytes[] = new byte[byteLength];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);

        return bytes;
    }
}
