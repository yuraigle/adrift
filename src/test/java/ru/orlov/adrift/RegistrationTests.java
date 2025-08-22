package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.orlov.adrift.controller.dto.RegisterRequestDto;
import ru.orlov.adrift.controller.dto.RegisterResponseDto;
import ru.orlov.adrift.initializr.FakeUserLoader;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationTests extends AbstractTest {

    @Autowired
    FakeUserLoader fakeUserLoader;

    @Test
    void invalidRegistrationForm() {
        RegisterRequestDto form = RegisterRequestDto.builder()
                .email(null)
                .username("tester")
                .password("tester")
                .build();

        ResponseEntity<String> response = apiRequestPost(
                "/api/auth/register", form, null, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().toLowerCase().contains("email");
    }

    @Test
    void registrationSuccess() {
        fakeUserLoader.deleteFakeUsers();

        RegisterRequestDto form = RegisterRequestDto.builder()
                .email("tester.123@tester")
                .username("tester.123")
                .password("tester")
                .build();

        ResponseEntity<RegisterResponseDto> response = apiRequestPost(
                "/api/auth/register", form, null, RegisterResponseDto.class
        );

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().getId() > 0;
        assert response.getBody().getToken() != null;

        fakeUserLoader.deleteFakeUsers();
    }

    @Test
    void registrationDuplicateEmail() {
        fakeUserLoader.deleteFakeUsers();

        RegisterRequestDto form = RegisterRequestDto.builder()
                .email("tester.123@tester")
                .username("tester.123")
                .password("tester").build();

        ResponseEntity<RegisterResponseDto> response = apiRequestPost(
                "/api/auth/register", form, null, RegisterResponseDto.class
        );

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        RegisterRequestDto form2 = RegisterRequestDto.builder()
                .email("tester.123@tester")
                .username("tester.124")
                .password("tester").build();

        ResponseEntity<String> resp2 = apiRequestPost(
                "/api/auth/register", form2, null, String.class
        );

        assert resp2.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert resp2.getHeaders().getContentType() != null;
        assert resp2.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert resp2.getBody() != null;
        assert resp2.getBody().toLowerCase().contains("email is already taken");

        RegisterRequestDto form3 = RegisterRequestDto.builder()
                .email("tester.124@tester")
                .username("tester.123")
                .password("tester").build();

        ResponseEntity<String> resp3 = apiRequestPost(
                "/api/auth/register", form3, null, String.class
        );

        assert resp3.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert resp3.getBody() != null;
        assert resp3.getBody().toLowerCase().contains("username is already taken");

        fakeUserLoader.deleteFakeUsers();
    }

}
