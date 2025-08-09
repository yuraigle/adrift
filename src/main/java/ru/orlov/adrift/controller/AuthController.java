package ru.orlov.adrift.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/api/auth/login", produces = "application/json")
    public AuthResponseDto login(
            @Valid @RequestBody AuthRequestDto request
    ) throws AppAuthException {
        AuthResponseDto response = new AuthResponseDto();
        response.setUsername(request.getUsername());
        return response;
    }

    @Data
    public static class AuthRequestDto {

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Data
    public static class AuthResponseDto {
        private Long id;
        private String username;
        private String token;
    }
}
