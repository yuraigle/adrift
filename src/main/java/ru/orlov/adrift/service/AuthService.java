package ru.orlov.adrift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${app.jwt-salt}")
    private String jwtSalt;

    @Value("${app.jwt-duration-hours:12}")
    private Long jwtDuration;

}
