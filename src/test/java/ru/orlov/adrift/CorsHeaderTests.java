package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.orlov.adrift.controller.dto.LoginRequestDto;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorsHeaderTests extends AbstractTest {

    private boolean hasCorsHeaders(ResponseEntity<String> response) {
        if (response.getHeaders().containsKey("Access-Control-Allow-Origin")) {
            return true;
        }

        List<String> vary = response.getHeaders().getVary();
        return vary.contains("Origin");
    }

    @Test
    void malformedPostRequestContainsCorsHeaders() {
        LoginRequestDto form = new LoginRequestDto("", null);
        ResponseEntity<String> response = apiRequestPost("/api/auth/register", form, null, String.class);

        assert hasCorsHeaders(response);
    }

    @Test
    void emptyPostRequestContainsCorsHeaders() {
        ResponseEntity<String> response = apiRequestPost("/api/auth/login", null, null, String.class);

        assert hasCorsHeaders(response);
    }

    @Test
    void apiNotFoundPageContainsCorsHeaders() {
        ResponseEntity<String> response = apiRequestGet("/api/invalid");

        assert hasCorsHeaders(response);
    }
}
