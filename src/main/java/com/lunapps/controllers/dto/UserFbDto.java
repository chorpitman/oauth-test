package com.lunapps.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Created by olegchorpita on 7/10/17.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFbDto {
    Long id;
    String first_name;
    String last_name;
    String email;
    String profileUrl;
}
