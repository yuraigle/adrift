package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
        ResponseEntity<String> resp = apiRequestPost("/api/auth/login", form, null, String.class);

        assert resp.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert resp.getBody() != null;
        assert resp.getBody().contains("messages");
        assert resp.getBody().contains("username");
        assert resp.getBody().contains("password");
    }

    @Test
    void loginWithWrongPassword() {
        LoginRequestDto form = new LoginRequestDto("admin", "wrong");
        ResponseEntity<String> resp = apiRequestPost("/api/auth/login", form, null, String.class);

        assert resp.getStatusCode() == HttpStatus.UNAUTHORIZED;
        assert resp.getBody() != null;
        assert resp.getBody().contains(" is incorrect");
    }

    @Test
    void loginSuccessResponseContainsToken() {
        LoginRequestDto form = new LoginRequestDto("admin", "admin");
        ResponseEntity<String> resp = apiRequestPost("/api/auth/login", form, null, String.class);

        assert resp.getStatusCode() == HttpStatus.OK;
        assert resp.getBody() != null;
        assert resp.getBody().contains("token");
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
