package com.lunapps.controllers.dto.adverts.park;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.ParkingType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class AdvertParkUpdateDto {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

    @NotNull
    private Long id;

    @NotNull
    private Long betoParkProfileId;

    @NotNull
    private Long betoParkProfileCarId;

    @FutureOrPresent
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime availabilityTimeFrom;

    @FutureOrPresent
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime availabilityTimeTo;

    @NotNull
    @Size(max = 255)
    private String parkingLocation;

    @NotNull
    private Float parkingCharge;

    //todo remove after test user rate
    @NotNull
    private Double userRate;

    @NotNull
    private Long waitingTime;

    @NotNull
    private ParkingType parkingType;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

//    @NotNull
//    private Double carWidth;

    @NotNull
    private Double carLength;
}
