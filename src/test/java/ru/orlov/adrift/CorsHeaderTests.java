package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorsHeaderTests extends AbstractTest {

    private boolean hasCorsHeaders(ResponseEntity<String> response) {
        if (response.getHeaders().containsKey("Access-Control-Allow-Origin")) {
            return true;
        }

        List<String> vary = response.getHeaders().get("Vary");
        return vary != null && vary.contains("Origin");
    }

    @Test
    void apiPageContainsCorsHeaders() {
        ResponseEntity<String> response = testRequestGet("/api/version");

        assert hasCorsHeaders(response);
    }

    @Test
    void error1PageContainsCorsHeaders() {
        Map<String, String> req = Map.of("password", "");
        ResponseEntity<String> response = testRequestPost("/api/auth/register", req);

        assert hasCorsHeaders(response);
    }

    @Test
    void error2PageContainsCorsHeaders() {
        ResponseEntity<String> response = testRequestPost("/api/auth/login", null);

        assert hasCorsHeaders(response);
    }

}
