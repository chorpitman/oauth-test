package com.lunapps.controllers.dto;

import com.lunapps.models.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupportCreateDto {

    @NotNull
    @Size(min = 20, max = 512, message = "message size should be between 20 and 512 symbols")
    private String description;

    @NotNull
    @Size(min = 10, max = 100, message = "title size should be between 10 and 100 symbols")
    private String title;

    @NotNull
    private TicketType ticketType;
}