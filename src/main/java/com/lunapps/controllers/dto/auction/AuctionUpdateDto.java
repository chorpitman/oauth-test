package com.lunapps.controllers.dto.auction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.Rating;
import com.lunapps.models.auction.AuctionStatus;
import com.lunapps.models.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.lunapps.models.deal.Deal.DATE_PATTERN_ISO_8601;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuctionUpdateDto {
    private Long id;
    private Long jobSeekerId;
    private String jobSeekerAvatar;
    private Set<Rating> jobSeekerRating;

    private Long advertOwnerId;
    private String jobOwnerAvatar;
    private Set<Rating> jobOwnerRating;

    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime startTime;
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime finishTime;
    private Double price;
    private List<Transaction> transactions = new ArrayList<>();
}
