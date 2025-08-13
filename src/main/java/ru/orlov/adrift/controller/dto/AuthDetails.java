package ru.orlov.adrift.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthDetails {
    public Long id;
    public String username;
}
