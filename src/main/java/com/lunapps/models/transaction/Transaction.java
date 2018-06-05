package com.lunapps.models.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lunapps.models.PaymentSystem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

import static com.lunapps.models.User.DATE_PATTERN_ISO_8601;

@Entity
@Table(name = "transactions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "id_payer", nullable = false)
    private Long payerId;

    @Column(name = "payment_system")
    @Enumerated(EnumType.STRING)
    private PaymentSystem paymentSystem;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "description")
    @Enumerated(EnumType.STRING)
    private TransactionDescription description;

    @Column(name = "way")
    @Enumerated(EnumType.STRING)
    private TransactionWay transactionWay;

    @Column(name = "created")
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime created;

    @Column(name = "updated")
    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime updated;
}