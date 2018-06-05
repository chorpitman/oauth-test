package com.lunapps.models.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JobAdvertFilter {
    private Long jobCategoryId;
    private Long jobSubCategoryId;
    private Long distance;
    private Double longitude;
    private Double latitude;
    private Long rewardMin;
    private Long rewardMax;
    private Long workTimeMin;
    private Long workTimeMax;
    //todo remove after test user rate
    private Double userRate;
}
