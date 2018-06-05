package com.lunapps.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.deal.Deal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "device_udid")
    private String deviceUDID;

    @Column(name = "is_active", nullable = false)
    private Boolean isUserAccountActive = Boolean.FALSE;

    @Column(name = "facebook_user_id")
    private String fbUserId;

    @Column(name = "email_token")
    private String emailToken;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "address")
    private String address;

    @Column(name = "pay_system")
    @Enumerated(EnumType.STRING)
    private PaymentSystem paymentSystem;

    @Column(name = "user_balance", precision = 7, scale = 2)
    private BigDecimal balance;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    //todo set authorities remove
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "betojob_profile_id")
    private BetoJobProfile betoJobProfile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "betopark_profile_id")
    private BetoParkProfile betoParkProfile;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user_id")
    private Set<Photo> photos;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "created")
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime createdDate;

    @Column(name = "updated")
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime updatedDate;

    @Column(name = "last_login_date")
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime loginDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_deals",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "deal_id")})
    private List<Deal> deals;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_rating",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "rating_id")})
    private Set<Rating> userRating;
}