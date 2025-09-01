package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.orlov.adrift.controller.dto.AuthDetails;
import ru.orlov.adrift.controller.dto.LoginRequestDto;
import ru.orlov.adrift.service.AuthService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginFormTests extends AbstractTest {

    @Autowired
    private AuthService authService;

    @Test
    void loginMalformedContainsErrorMessages() {
        LoginRequestDto form = new LoginRequestDto("", "");
        ResponseEntity<String> response = apiRequestPost("/api/auth/login", form, null, String.class);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().contains("email");
        assert response.getBody().contains("password");
    }

    @Test
    void loginWithWrongPassword() {
        LoginRequestDto form = new LoginRequestDto("admin@admin", "wrong");
        ResponseEntity<String> response = apiRequestPost("/api/auth/login", form, null, String.class);

        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages");
    }

    @Test
    void loginSuccessResponseContainsToken() {
        LoginRequestDto form = new LoginRequestDto("admin@admin", "admin");
        ResponseEntity<String> response = apiRequestPost("/api/auth/login", form, null, String.class);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("token");
    }

    @Test
    void generatedTokenIsValid() throws Exception {
        String token = retrieveAdminToken();

        assert token != null;
        assert !token.isEmpty();

        AuthDetails details = authService.parseToken(token);

        assert details != null;
        assert details.getId() != null;
        assert details.getUsername() != null;
        assert details.getUsername().equals("admin");
    }

}
