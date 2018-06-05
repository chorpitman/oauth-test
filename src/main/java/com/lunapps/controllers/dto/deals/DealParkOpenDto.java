package com.lunapps.controllers.dto.deals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.controllers.dto.auction.AuctionUpdateDto;
import com.lunapps.models.Car;
import com.lunapps.models.ParkingType;
import com.lunapps.models.deal.Claim;
import com.lunapps.models.deal.DealInfo;
import com.lunapps.models.deal.DealStatus;
import com.lunapps.models.deal.DealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealParkOpenDto {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";
    private Long dealId;
    private Long authorId;
    private String authorAvatar;
    private String authorFirstName;
    private String authorLastName;
    private Long clientId;
    private String clientAvatar;
    private String clientFirstName;
    private String clientLastName;
    private Long serviceId;
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
    private Car car;
    //deals
    private DealType dealType;
    private DealStatus dealStatus;
    private DealInfo dealInfo;
    private Double amountDeal;
    private Double authorCommission;
    private Double clientCommission;
    private String historyOfNegotiation;
    private Boolean isOwnerAccept = Boolean.FALSE;
    private Boolean isClientAccept = Boolean.FALSE;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime created;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime updated;
    //about deals auc
    private List<AuctionUpdateDto> auctionList;
    private List<Claim> claimList;
}
