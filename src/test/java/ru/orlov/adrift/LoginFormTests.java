package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.orlov.adrift.controller.dto.LoginRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginFormTests extends AbstractTest {

    @Test
    void loginMalformedContainsErrorMessages() {
        LoginRequestDto form = new LoginRequestDto("", "");
        ResponseEntity<String> response = apiRequestPost("/api/auth/login", form, null, String.class);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().contains("username");
        assert response.getBody().contains("password");
    }

    @Test
    void loginSuccessResponseContainsToken() {
        LoginRequestDto form = new LoginRequestDto("admin", "admin");
        ResponseEntity<String> response = apiRequestPost("/api/auth/login", form, null, String.class);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().contains("token");
    }

}
