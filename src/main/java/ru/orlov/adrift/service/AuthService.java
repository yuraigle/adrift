package ru.orlov.adrift.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.domain.User;
import ru.orlov.adrift.domain.UserRepository;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.trimToNull;
import static ru.orlov.adrift.domain.User.hashPassword;
import static ru.orlov.adrift.domain.User.verifyPassword;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${app.jwt-salt}")
    private String jwtSalt;

    @Value("${app.jwt-duration-hours:12}")
    private Long jwtDurationHours;

    private final UserRepository userRepository;

    public AuthDetails authenticate(
            String username,
            String password
    ) throws AppAuthException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppAuthException("User not found"));

        String hashedPassword = user.getPassword();
        if (!verifyPassword(password, hashedPassword)) {
            throw new AppAuthException("Username or password incorrect");
        }

        AuthDetails details = new AuthDetails();
        details.setId(user.getId());
        details.setUsername(user.getUsername());
        return details;
    }

    public AuthDetails register(
            String email,
            String username,
            String password
    ) throws AppAuthException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new AppAuthException("Username already taken");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AppAuthException("Email already taken");
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(hashPassword(password));
        user.setCreated(LocalDateTime.now());

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            throw new AppAuthException(e.getMessage());
        }

        AuthDetails details = new AuthDetails();
        details.setId(savedUser.getId());
        details.setUsername(savedUser.getUsername());
        return details;
    }

    public String generateToken(AuthDetails details) {
        long expirationMs = 1000 * 60 * 60 * jwtDurationHours;

        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expirationMs))
                .subject(details.getUsername())
                .claim("id", details.getId())
                .signWith(getSigningKey())
                .compact();
    }

    public AuthDetails parseToken(String token) throws AppException {
        if (token == null || token.isEmpty()) {
            throw new AppException("Authorization is missing in request", HttpStatus.UNAUTHORIZED);
        }

        JwtParser parser = Jwts.parser().verifyWith(getSecretKey()).build();

        try {
            if (token.startsWith("Bearer ")) {
                token = token.trim().substring(7);
            }

            Jwt<?, ?> jwt = parser.parse(token);
            DefaultClaims payload = (DefaultClaims) jwt.getPayload();

            AuthDetails details = new AuthDetails();
            details.setUsername(trimToNull(payload.getSubject()));
            details.setId(Long.valueOf(payload.get("id").toString()));

            return details;
        } catch (MalformedJwtException e) {
            throw new AppException("Invalid auth token", HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException e) {
            throw new AppAuthException("Auth token expired");
        } catch (Exception e) {
            throw new AppAuthException(e.getMessage());
        }
    }

    private SecretKeySpec getSecretKey() {
        return new SecretKeySpec(
                this.jwtSalt.getBytes(StandardCharsets.UTF_8),
                getSigningKey().getAlgorithm()
        );
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.jwtSalt.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Data
    @NoArgsConstructor
    public static class AuthDetails {
        public Long id;
        public String username;
    }
}
