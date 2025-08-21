package ru.orlov.adrift.controller.dto;

import lombok.Data;

@Data
public class RegisterResponseDto {

    private Long id;
    private String username;
    private String token;

}
