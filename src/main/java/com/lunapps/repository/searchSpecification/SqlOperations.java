package com.lunapps.repository.searchSpecification;

import lombok.Getter;

@Getter
public enum SqlOperations {
    EQUAL("="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL_TO(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("<="),
    BETWEEN("BETWEEN"),
    BETWEEN_DATES("BETWEEN_DATES"),
    LIKE("LIKE");

    final String sqlOperation;

    SqlOperations(final String sqlOperation) {
        this.sqlOperation = sqlOperation;
    }
}
