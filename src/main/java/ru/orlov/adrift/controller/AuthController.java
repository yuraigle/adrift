package ru.orlov.adrift.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.controller.dto.AuthDetails;
import ru.orlov.adrift.controller.dto.LoginRequestDto;
import ru.orlov.adrift.controller.dto.LoginResponseDto;
import ru.orlov.adrift.controller.dto.RegisterRequestDto;
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
        AuthDetails details = authService.authenticate(
                request.getUsername(), request.getPassword()
        );

        LoginResponseDto response = new LoginResponseDto();
        response.setId(details.getId());
        response.setUsername(details.getUsername());
        response.setToken(authService.generateToken(details));
        return response;
    }

    @PostMapping(value = "/api/auth/register", produces = "application/json")
    public LoginResponseDto register(
            @Valid @RequestBody RegisterRequestDto request
    ) throws AppAuthException {
        AuthDetails details = authService.register(
                request.getEmail(), request.getUsername(), request.getPassword()
        );

        LoginResponseDto response = new LoginResponseDto();
        response.setId(details.getId());
        response.setUsername(details.getUsername());
        response.setToken(authService.generateToken(details));
        return response;
    }

}
