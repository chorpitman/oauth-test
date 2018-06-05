package com.lunapps.controllers.dto.deals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.Rating;
import com.lunapps.models.auction.Auction;
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

import static com.lunapps.models.deal.Deal.DATE_PATTERN_ISO_8601;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealDetailDto {
    private Long id;
    private String authorFirstName;
    private String authorLastName;
    private String authorAvatar;
    private String clientFirstName;
    private String clientLastName;
    private String clientAvatar;
    private Set<Rating> clientRatings;
    private DealType dealType;
    private DealStatus dealStatus;
    private DealInfo dealInfo;
    private Double amountDeal;
    private Double laborerCommission;
    private Double homeyCommission;
    private Boolean isOwnerAccept = Boolean.FALSE;
    private Boolean isLaborerAccept = Boolean.FALSE;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime created;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime updated;
    private List<Auction> auctionList;
    private List<Claim> claimList;
}
