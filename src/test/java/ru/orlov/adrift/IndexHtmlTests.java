package ru.orlov.adrift;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexHtmlTests extends AbstractTest {

    @Test
    void indexHtmlIsShown() {
        ResponseEntity<String> response = htmlRequestGet("/");

        int statusCode = response.getStatusCode().value();
        assert statusCode == 200 || statusCode == 404;

        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.TEXT_HTML);

        assert response.getBody() != null;
        assert response.getBody().contains("<body>");
        assert response.getBody().contains("<title>");
    }

    @Test
    void ssrHtmlAreShown() {
        List<String> routes = List.of("/about", "/auth/login", "/auth/register");

        for (String route : routes) {
            ResponseEntity<String> response = htmlRequestGet(route);

            int statusCode = response.getStatusCode().value();
            assert statusCode == 200 || statusCode == 404;

            assert response.getHeaders().getContentType() != null;
            assert response.getHeaders().getContentType().includes(MediaType.TEXT_HTML);

            assert response.getBody() != null;
            assert response.getBody().contains("<body>");
            assert response.getBody().contains("<title>");
        }
    }
}
