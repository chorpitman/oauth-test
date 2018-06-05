package com.lunapps.models.auction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lunapps.models.deal.Deal.DATE_PATTERN_ISO_8601;

@Entity
@Table(name = "auctions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Auction {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "job_seeker_id")
    private Long jobSeekerId;

    @Column(name = "advert_owner_id")
    private Long advertOwnerId;

    @Column(name = "auction_status")
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @Column(name = "start_time")
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime startTime;

    @Column(name = "finish_time")
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime finishTime;

    @Column(name = "price")
    private Double price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> transactions = new ArrayList<>();
}
