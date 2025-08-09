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
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto request
    ) throws AppAuthException {
        AuthService.AuthDetails details = authService
                .authenticate(request.getUsername(), request.getPassword());
        String token = authService.generateToken(details);

        LoginResponseDto response = new LoginResponseDto();
        response.setId(details.getId());
        response.setUsername(details.getUsername());
        response.setToken(token);
        return response;
    }

    @Data
    public static class LoginRequestDto {

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Data
    public static class LoginResponseDto {
        private Long id;
        private String username;
        private String token;
    }
}
