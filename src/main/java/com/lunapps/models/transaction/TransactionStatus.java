package com.lunapps.models.transaction;

public enum TransactionStatus {
    APPROVED(""), IN_PROGRESS(""), REJECTED(""), CONFIRMED("");

    final String name;

    TransactionStatus(String name) {
        this.name = name;
    }
}
