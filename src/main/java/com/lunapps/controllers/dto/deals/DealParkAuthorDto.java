package com.lunapps.controllers.dto.deals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.AdvertStatus;
import com.lunapps.models.Car;
import com.lunapps.models.ParkingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DealParkAuthorDto {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

    private Long advertId;
    private Long betoParkProfileId;
    private Long betoParkProfileCarId;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime availabilityTimeFrom;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime availabilityTimeTo;
    private String parkingLocation;
    private Float parkingCharge;
    //todo remove after test user rate
    private Double userRate;
    private Long waitingTime;
    private ParkingType parkingType;
    private Double latitude;
    private Double longitude;
    private Double carLength;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime updated;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime created;
    private AdvertStatus advertStatus;
    //author info
    private String firstname;
    private String lastname;
    private String avatarUrl;
    private Car car;
    //deal
    List<DealDetailDto> deals;
}
