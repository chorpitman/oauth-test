package com.lunapps.models.transaction;


public enum TransactionType {
    REFILL("REFILL"),
    REFUND("REFUND"),
    CHARGED("CHARGED"),
    COMMITMENT_FEE("BLOCK_10%"),
    SYSTEM_FEE("SYSTEM_COMMISSION_6%"),
    PAYMENT("PAYMENT"),
    PENALTY("PENALTY_2.5%");

    final String name;

    TransactionType(String name) {
        this.name = name;
    }
}
