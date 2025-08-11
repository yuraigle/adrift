package ru.orlov.adrift.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
