package com.lunapps.services;

import java.util.HashMap;

/**
 * Created by olegchorpita on 8/2/17.
 */
public interface TokenService {
//    String createJwtRefreshToken(UserDetails userDetails, Device device);

//    String createJwtAccessToken(UserDetails userDetails, Device device);

    HashMap<String, String> generateMailToken(String userEmailToken, String userDbToken);

    byte[] generateRandomBytes(int length);
}
