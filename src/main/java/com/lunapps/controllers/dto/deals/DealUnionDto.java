package com.lunapps.controllers.dto.deals;

import com.lunapps.controllers.dto.adverts.AdvertJobDetailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DealUnionDto {
    private List<DealOpenDto> clientOpenDeals;
    private List<AdvertJobDetailDto> authorOpenDeals;
}