package com.lunapps.controllers.dto.adverts.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.Category;
import com.lunapps.models.Photo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class AdvertJobDto {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

    private Long id;

    @NotNull
    private String subject;

    @NotNull
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private String description;

    @NotNull
    private Long workDuration;

    @NotNull
    private Double workReward;

    @NotNull
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime mustDoneWorkTo;

    @NotNull
    private String requirement;

    private Category category;
    //todo remove after test user rate
    private Double userRate;

    @NotNull
    private Set<Photo> photoList;

    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime createdDate;

    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime updatedDate;

    private String avatarUrl;

    private String firstname;

    private String lastname;

    private Boolean isCurrentUserAdvert;

    private Boolean isPhotoRequestConfirmationNeed;

    private Boolean isJobDonePhotoDownloaded;

}
