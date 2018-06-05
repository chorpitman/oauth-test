package com.lunapps.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserAdminPageDto {
    private List<UserAdminDto> userAdminDto;
    private Long totalElements;
    private Integer totalPages;
}