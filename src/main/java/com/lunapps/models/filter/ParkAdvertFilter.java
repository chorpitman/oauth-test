package com.lunapps.models.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.ParkingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkAdvertFilter {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime availabilityTimeFrom;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime availabilityTimeTo;
    private Double longitude;
    private Double latitude;
    private Long distance;
    private Long rewardMin;
    private Long rewardMax;
    //todo remove after test user rate
    private Double userRate;
    private Long parkingLength;
    private ParkingType parkingType;
}