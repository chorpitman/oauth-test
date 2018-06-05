package com.lunapps.controllers.dto;

import com.lunapps.models.BetoJobProfile;
import com.lunapps.models.BetoParkProfile;
import com.lunapps.models.Gender;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileTokenDto {
    private String firstname;
    private String lastname;
    private String nickname;
    private String email;
    private Boolean isUserAccountActive;
    private BetoJobProfile betoJobProfile;
    private BetoParkProfile betoParkProfile;
    private Gender gender;
    private Integer age;
    private String avatarUrl;
    private String address;
}