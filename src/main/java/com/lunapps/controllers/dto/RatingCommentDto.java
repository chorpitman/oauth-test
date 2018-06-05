package com.lunapps.controllers.dto;

import com.lunapps.models.RatingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingCommentDto {

    private Long id;

    private String comment;

    private Long rating;

    private Long dealId;

    private RatingType ratingType;

    private Long feedbackAuthor;

    private String feedbackAuthorFirstName;

    private String feedbackAuthorLastName;

    private String feedbackAuthorAvatar;

    private ZonedDateTime created;
}
