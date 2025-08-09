package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorsHeaderTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void apiPageContainsCorsHeaders() {
        ResponseEntity<String> response = restTemplate
                .getForEntity(url("/api/version"), String.class);

        assert response.getHeaders().containsKey("Access-Control-Allow-Origin");
    }

    @Test
    void errorPageContainsCorsHeaders() {
        Map<String, String> req = Map.of("password", "");

        ResponseEntity<String> response = restTemplate
                .postForEntity(url("/api/auth/login"), req, String.class);

        assert response.getHeaders().containsKey("Access-Control-Allow-Origin");
    }

}
