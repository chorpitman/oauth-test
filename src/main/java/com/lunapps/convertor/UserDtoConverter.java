package com.lunapps.convertor;

import com.lunapps.controllers.dto.UserAdminDto;
import com.lunapps.controllers.dto.UserFbDto;
import com.lunapps.controllers.dto.UserProfileDto;
import com.lunapps.controllers.dto.UserProfileTokenDto;
import com.lunapps.models.Authority;
import com.lunapps.models.BetoJobProfile;
import com.lunapps.models.BetoParkProfile;
import com.lunapps.models.Gender;
import com.lunapps.models.PaymentSystem;
import com.lunapps.models.Photo;
import com.lunapps.models.PhotoType;
import com.lunapps.models.User;
import com.lunapps.models.UserRole;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by olegchorpita on 9/12/17.
 */
@Component
public class UserDtoConverter {
    @Autowired
    private BCryptPasswordEncoder encoder;

    public User convert(UserProfileDto userDto) {
        //todo need reimplement this method
        Objects.requireNonNull(userDto);
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
//        user.setIsUserAccountActive(userDto.getIsUserAccountActive());
        user.setAge(userDto.getAge());
        user.setBalance(userDto.getBalance());
        user.setGender(userDto.getGender());
        user.setPaymentSystem(userDto.getPaymentSystem());
        user.setAddress(userDto.getAddress());
        user.setAvatarUrl(userDto.getAvatarUrl());
//        user.setCreatedDate(userDto.getCreatedDate());
//        user.setUpdatedDate(userDto.getUpdatedDate());
//        user.setAuthorities(userDto.getAuthorities());
        user.setBetoJobProfile(userDto.getBetoJobProfile());
        user.setBetoParkProfile(userDto.getBetoParkProfile());

        return user;
    }

    public UserProfileDto convert(User user) {
        Objects.requireNonNull(user);
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFirstname(user.getFirstname());
        profileDto.setLastname(user.getLastname());
        profileDto.setNickname(user.getNickname());
        profileDto.setEmail(user.getEmail());
        profileDto.setIsUserAccountActive(user.getIsUserAccountActive());
        profileDto.setGender(user.getGender());
        profileDto.setAge(user.getAge());
        profileDto.setBalance(user.getBalance());
        profileDto.setAddress(user.getAddress());
        profileDto.setPaymentSystem(user.getPaymentSystem());
//        userDtoToken.setCreatedDate(user.getCreatedDate());
//        userDtoToken.setUpdatedDate(user.getUpdatedDate());
        profileDto.setBetoJobProfile(removeRedundantFields(user.getBetoJobProfile()));
        profileDto.setBetoParkProfile(user.getBetoParkProfile());
        profileDto.setAvatarUrl(getAvatarPhoto(user));
        profileDto.setUserRating(user.getUserRating());

        return profileDto;
    }

    public UserAdminDto convertToAdminDto(User user) {
        Objects.requireNonNull(user);
        return UserAdminDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .isUserAccountActive(user.getIsUserAccountActive())
                .fbUserId(user.getFbUserId())
                .gender(user.getGender())
                .age(user.getAge())
                .address(user.getAddress())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
//                .userRoles(getUserRole(user.getAuthorities()))
                .paymentSystem(user.getPaymentSystem())
                .betoJobProfile(removeRedundantFields(user.getBetoJobProfile()))
                .betoParkProfile(removeRedundantFields(user.getBetoParkProfile()))
                .photos(user.getPhotos())
                .avatarUrl(user.getAvatarUrl())
                .loginDate(user.getLoginDate())
                .userRating(user.getUserRating())
                .build();
    }

    public UserProfileTokenDto convertToTokenProfileDto(User user) {
        Objects.requireNonNull(user);
        UserProfileTokenDto tokenDto = UserProfileTokenDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .isUserAccountActive(user.getIsUserAccountActive())
                .betoParkProfile(removeRedundantFields(user.getBetoParkProfile()))
                .betoJobProfile(removeRedundantFields(user.getBetoJobProfile()))
                .gender(user.getGender())
                .age(user.getAge())
                .avatarUrl(user.getAvatarUrl())
                .address(user.getAddress())
                .build();

        return tokenDto;
    }

    public User convertFbDtoToUser(final UserFbDto userFbDto) {
        //todo re-implement - create oauth2 springsocial
        User user = User.builder()
                .firstname(userFbDto.getFirst_name())
                .lastname(userFbDto.getLast_name())
                .nickname(null)
                .email(generateEmail(userFbDto))
                .isUserAccountActive(Boolean.TRUE)
                .gender(Gender.NOT_SELECTED)
                .age(null)
                .address(null)
                .createdDate(ZonedDateTime.now())
                .updatedDate(null)
                .authorities(Collections.singleton(Authority.builder().userRole(UserRole.ROLE_USER).build()))
                .paymentSystem(PaymentSystem.NOT_SELECTED)
                .balance(BigDecimal.ZERO)
                .betoJobProfile(BetoJobProfile.builder().enable(Boolean.FALSE).jobAdverts(Collections.EMPTY_SET).build())
                .betoParkProfile(BetoParkProfile.builder().enable(Boolean.FALSE).parkAdvert(Collections.EMPTY_SET).build())
                .password(encoder.encode(String.valueOf(userFbDto.getId())))
                //todo think about encode fb user id
                .fbUserId(String.valueOf(userFbDto.getId()))
                .photos(Collections.EMPTY_SET)
                .avatarUrl(userFbDto.getProfileUrl())
                .build();

        return user;
    }

    public UserFbDto convertUserToFbDto(final User user) {
        if (Objects.nonNull(user)) {
            UserFbDto fbDto = new UserFbDto();

            fbDto.setId(Long.parseLong(user.getFbUserId()));
            fbDto.setFirst_name(user.getFirstname());
            fbDto.setLast_name(user.getLastname());
            fbDto.setEmail(user.getEmail());
            fbDto.setProfileUrl(getAvatarPhoto(user));

            return fbDto;
        }
        return null;
    }

    private String getAvatarPhoto(User user) {
        Set<Photo> photos = user.getPhotos();
        if (photos.size() == 0) {
            String url = user.getAvatarUrl();
            return url;
        }

        for (Photo photo : photos) {
            if (Objects.equals(photo.getPhotoType().getPhotoType(), PhotoType.USER_AVATAR.getPhotoType()))
                return photo.getUrl();
            else return null;
        }
        return null;
    }

    private BetoParkProfile removeRedundantFields(BetoParkProfile profile) {
        BetoParkProfile parkProfile = new BetoParkProfile();
        parkProfile.setId(profile.getId());
        parkProfile.setEnable(profile.getEnable());

        return parkProfile;
    }

    private BetoJobProfile removeRedundantFields(BetoJobProfile profile) {
        BetoJobProfile jobProfile = new BetoJobProfile();
        jobProfile.setId(profile.getId());
        jobProfile.setEnable(profile.getEnable());

        return jobProfile;
    }

    private String generateEmail(final UserFbDto userFbDto) {
        final String betoshookEmailDomain = "@fb.betoshook.com";

        if (Objects.nonNull(userFbDto)) {
            if (Objects.nonNull(userFbDto.getEmail())) {
                return userFbDto.getEmail();
            }
            StringBuilder emailBuilder = new StringBuilder();
            String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
            return emailBuilder
                    .append(userFbDto.getId())
                    .append("_")
                    .append(randomAlphanumeric)
                    .append(betoshookEmailDomain)
                    .toString();
        }
        return null;
    }

    private List<UserRole> getUserRole(final List<Authority> authority) {
        ArrayList<UserRole> list = new ArrayList<>();
        for (Authority role : authority) {
            list.add(role.getUserRole());
        }
        return list;
    }
}
