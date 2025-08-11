package ru.orlov.adrift.controller.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private Long id;
    private String username;
    private String token;

}
