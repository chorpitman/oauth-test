package com.lunapps.controllers.dto.adverts.job;

import com.lunapps.models.Photo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class AdvertJobUpdatePhotoDto {
    @NotNull
    private Long jobAdvertId;
    @NotNull
    private Long dealId;
    @NotNull
    private Set<Photo> photoList;
}
