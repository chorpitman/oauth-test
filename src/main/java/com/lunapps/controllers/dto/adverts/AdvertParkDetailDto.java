//package com.lunapps.controllers.dto.adverts;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.lunapps.controllers.dto.deals.DealDetailDto;
//import com.lunapps.models.Photo;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Set;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class AdvertParkDetailDto {
//    public static final String DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss Z";
//
//    private Long id;
//    private String subject;
//    private String address;
//    private String description;
//
//    private Long workDuration;
//    private Double workReward;
//    private String requirement;
//    private Long userRate;
//    private Set<Photo> photoList;
//    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    private ZonedDateTime createdDate;
//    @JsonFormat(pattern = DATE_PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    private ZonedDateTime updatedDate;
//    private List<DealDetailDto> dealList;
//}
//todo remove class
