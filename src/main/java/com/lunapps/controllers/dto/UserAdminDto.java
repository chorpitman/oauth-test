package com.lunapps.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.BetoJobProfile;
import com.lunapps.models.BetoParkProfile;
import com.lunapps.models.Gender;
import com.lunapps.models.PaymentSystem;
import com.lunapps.models.Photo;
import com.lunapps.models.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminDto {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

    private Long id;

    private String firstname;

    private String lastname;

//    private String password;

    private String nickname;

    private String email;

    private Boolean isUserAccountActive = Boolean.FALSE;

    private String fbUserId;

//    private String emailToken;

    private Gender gender;

    private Integer age;

    private String address;

    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime createdDate;

    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime updatedDate;

//    private List<UserRole> userRoles;

    private PaymentSystem paymentSystem;

    private BigDecimal balance;

    private BetoJobProfile betoJobProfile;

    private BetoParkProfile betoParkProfile;

    private Set<Photo> photos;

    private String avatarUrl;

    private ZonedDateTime loginDate;

    private Set<Rating> userRating;
}
