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
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Set;

@Builder
@ToString
@Setter
@Getter
public class AdvertJobCreateDto {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

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
    @Positive(message = "work reward can not be zero or negative")
    private Double workReward;

    @FutureOrPresent
    @NotNull
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime mustDoneWorkTo;

    @NotNull
    private Long jobCategoryId;

    private Long jobSubCategoryId;

    @NotNull
    @Size(max = 1500, message = "field length can not me more then 1500 symbols")
    private String requirement;

    @NotNull(message = "filed photoList can not be null")
    private Set<Photo> photoList;

    @NotNull(message = "filed isPhotoRequestConfirmationNeed can not be null")
    private Boolean isPhotoRequestConfirmationNeed;
}
