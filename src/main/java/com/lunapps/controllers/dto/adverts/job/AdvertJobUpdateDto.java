package com.lunapps.controllers.dto.adverts.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.Photo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class AdvertJobUpdateDto {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

    @NotNull
    private Long id;

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    @Size(max = 255)
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    @Size(max = 1500)
    private String description;

    @NotNull
    private Long workDuration;

    @NotNull
    private Double workReward;

    @FutureOrPresent
    @NotNull
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime mustDoneWorkTo;

    @NotNull
    @Size(max = 1500)
    private String requirement;

    @NotNull
    private Long jobCategoryId;

    private Long jobSubCategoryId;

    private Boolean isPhotoRequestConfirmationNeed;

    private Set<Photo> photoList;
}
