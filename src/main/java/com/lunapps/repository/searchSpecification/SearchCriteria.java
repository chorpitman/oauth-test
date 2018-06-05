package com.lunapps.repository.searchSpecification;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private Long min;
    private Long max;
    private ZonedDateTime from;
    private ZonedDateTime to;

    public SearchCriteria(final String key, final String operation, final Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SearchCriteria(final String key, final String operation, Long min, Long max) {
        this.key = key;
        this.operation = operation;
        this.min = min;
        this.max = max;
    }

    public SearchCriteria(final String key, final String operation, final ZonedDateTime from, final ZonedDateTime to) {
        this.key = key;
        this.operation = operation;
        this.from = from;
        this.to = to;
    }
}
