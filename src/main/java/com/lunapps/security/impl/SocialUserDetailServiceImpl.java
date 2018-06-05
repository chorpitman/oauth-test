//package com.lunapps.security.impl;
//
//import com.lunapps.exception.user.UserRegistrationException;
//import com.lunapps.models.User;
//import com.lunapps.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.social.security.SocialUser;
//import org.springframework.social.security.SocialUserDetails;
//import org.springframework.social.security.SocialUserDetailsService;
//
//import java.util.Objects;
//
///**
// * Created by olegchorpita on 9/7/17. todo need for fb
// */
//public class SocialUserDetailServiceImpl implements SocialUserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    @Qualifier(value = "localUserDetailService")
//    private UserDetailsService userDetailService;
//
//    @Override
//    public SocialUserDetails loadUserByUserId(final String userId) throws UsernameNotFoundException {
//        User foundUserByFbId = userRepository.findByFbUserId(userId);
//        if (Objects.isNull(foundUserByFbId)) {
//            throw new UserRegistrationException("something went wrong");
//        }
//        return new SocialUser(null, null, null);
//    }
//}
