package com.lunapps.controllers.dto.deals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.controllers.dto.auction.AuctionUpdateDto;
import com.lunapps.models.Photo;
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
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DealOpenDto {
    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";

    private Long id; //dealId
    private Long homeyId;
    private String homeyAvatar;
    private String homeyFirstName;
    private String homeyLastName;
    private Long laborerId;
    private String laborerAvatar;
    private Long serviceId;
    private String serviceDescription;
    private String serviceCategory;
    private String subCategory;
    private Set<Photo> advertPhoto;
    private DealType dealType;
    private DealStatus dealStatus;
    private DealInfo dealInfo;
    private Double amountDeal;
    private Double laborerCommission;
    private Double homeyCommission;
    private String historyOfNegotiation;
    private Boolean isOwnerAccept = Boolean.FALSE;
    private Boolean isLaborerAccept = Boolean.FALSE;
    //new fields
    private Boolean isPhotoRequestConfirmationNeed;
    private Boolean isJobDonePhotoDownloaded;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime created;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime updated;
    private List<AuctionUpdateDto> auctionList;
    private List<Claim> claimList;
}
