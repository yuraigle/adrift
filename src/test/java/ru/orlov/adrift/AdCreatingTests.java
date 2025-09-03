package ru.orlov.adrift;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.Ad;
import ru.orlov.adrift.domain.AdField;
import ru.orlov.adrift.domain.AdOption;
import ru.orlov.adrift.domain.AdRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdCreatingTests extends AbstractTest {

    @Autowired
    AdRepository adRepository;

    @Test
    void createAdUnauthorized() {
        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #0");
        form.setCategory(1L);
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads", form, null, String.class
        );

        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);
    }

    @Test
    void createAdMalformed() {
        AdRequestDto form = new AdRequestDto();
        form.setCategory(null);
        form.setTitle(null);
        form.setPrice(null);

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads", form, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().contains("category");
        assert response.getBody().contains("title");
        assert response.getBody().contains("price");
    }

    @Test
    void createAdWithoutRequiredFields() {
        AdRequestDto form = new AdRequestDto();
        form.setCategory(2L);
        form.setTitle("Test AD #3");
        form.setPrice(BigDecimal.valueOf(0));
        form.getFields().add(new AdRequestDto.AdFieldDto(2L, null));

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads", form, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().contains("Missing required field");
    }

    @Test
    void createAdWithMultipleOptionField() {
        AdRequestDto form = new AdRequestDto();
        form.setCategory(2L);
        form.setTitle("Test AD #3");
        form.setPrice(BigDecimal.valueOf(0));
        form.getFields().add(new AdRequestDto.AdFieldDto(2L, "1")); // weekly
        form.getFields().add(new AdRequestDto.AdFieldDto(2L, "2")); // and monthly

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads", form, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().toLowerCase().contains("multiple");
    }

    @Test
    void createAdSuccessContainsCreatedId() {
        cleanupTestAds();

        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #0");
        form.setDescription("Test description");
        form.setCategory(1L);
        form.setPrice(BigDecimal.valueOf(0));

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads", form, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("\"id\":");

        cleanupTestAds();
    }

    @Test
    void createAdWithCustomFields() {
        cleanupTestAds();

        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #1");
        form.setDescription("Test description");
        form.setCategory(1L); // Houses for sale
        form.setPrice(BigDecimal.valueOf(101));
        form.setFields(List.of(
                new AdRequestDto.AdFieldDto(1L, "67.60"), // area
                new AdRequestDto.AdFieldDto(3L, "1985"), // construction year
                new AdRequestDto.AdFieldDto(4L, "https://example.com") // www
        ));

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads", form, token, String.class
        );

        // assert Ad is created
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;

        List<Ad> ads = adRepository.findByTitle("Test AD #1");
        assert !ads.isEmpty();

        Ad ad = ads.getLast();
        assert ad != null;

        // assert Ad Fields are created
        assert !ad.getFields().isEmpty();

        Optional<AdField> f1 = ad.getFields().stream()
                .filter(f -> f.getQuestion().getId() == 1L)
                .findAny();

        assert f1.isPresent();
        assert f1.get().getValDecimal().equals(BigDecimal.valueOf(67.60));

        cleanupTestAds();
    }

    @Test
    void createAdWithCheckboxFields() {
        cleanupTestAds();

        // create an Ad using rest api
        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #2");
        form.setCategory(1L); // Houses for sale
        form.setPrice(BigDecimal.valueOf(102));
        form.setFields(List.of(
                new AdRequestDto.AdFieldDto(6L, "5"), // Garage
                new AdRequestDto.AdFieldDto(6L, "6") // High Ceilings
        ));

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads", form, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;

        List<Ad> ads = adRepository.findByTitle("Test AD #2");
        assert !ads.isEmpty();

        Ad ad = ads.getLast();
        assert ad != null;

        // assert Ad Options are created
        assert !ad.getOptions().isEmpty();

        Optional<AdOption> f1 = ad.getOptions().stream()
                .filter(f -> f.getQuestion().getId() == 6L)
                .findAny();

        assert f1.isPresent();
        assert !f1.get().getOption().getName().isBlank();

        cleanupTestAds();
    }
}
