package ru.orlov.adrift;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    ResponseEntity<String> testRequestGet(String path) {
        return restTemplate.getForEntity(url(path), String.class);
    }

    ResponseEntity<String> testRequestPost(String path, Map<String, String> form) {
        String encodedFormBody = JSONObject.wrap(form).toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json;charset=UTF-8");

        return restTemplate.exchange(
                url(path),
                HttpMethod.POST,
                new HttpEntity<>(encodedFormBody, headers),
                String.class
        );
    }

}
