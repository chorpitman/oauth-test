package com.lunapps.controllers.dto;

import com.lunapps.models.BetoJobProfile;
import com.lunapps.models.BetoParkProfile;
import com.lunapps.models.Gender;
import com.lunapps.models.PaymentSystem;
import com.lunapps.models.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by i4790k on 11.09.2017.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private String firstname;

    private String lastname;

    private String nickname;

    private String email;

    private Boolean isUserAccountActive = Boolean.FALSE;

    //todo ask dima about @Null
    private BetoJobProfile betoJobProfile;

    //todo ask dima about @Null
    private BetoParkProfile betoParkProfile;

    @NotNull
    private Gender gender;
    @NotNull
    private Integer age;

    private PaymentSystem paymentSystem;

    private BigDecimal balance;

    @NotNull
    private String address;

    private String avatarUrl;
    //rating system
    private Set<Rating> userRating;
}
