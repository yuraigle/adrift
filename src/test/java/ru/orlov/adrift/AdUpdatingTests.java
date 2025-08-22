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
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.service.AdService;

import java.math.BigDecimal;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdUpdatingTests extends AbstractTest {

    @Autowired
    private AdService adService;

    @Autowired
    private AdRepository adRepo;

    @Test
    void updateAdUnauthorized() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto form = new AdRequestDto();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, form, null, String.class
        );

        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);
    }

    @Test
    void updateAnotherUserAdUnauthorized() throws Exception {
        Long id = generateRandomTestAd(); // created by admin
        assert id != null;

        String token = retrieveTesterToken();
        AdRequestDto req = new AdRequestDto();
        req.setCategory(1L);
        req.setTitle("Test AD #100 Updated Title");
        req.setPrice(BigDecimal.valueOf(0));

        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, req, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.FORBIDDEN;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().contains("belongs to another user");
    }

    @Test
    void updateAdMalformed() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto req = new AdRequestDto();
        req.setCategory(null);
        req.setTitle(null);
        req.setPrice(null);

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, req, token, String.class
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
    void updateAdForRentSuccessful() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto req = new AdRequestDto();
        req.setCategory(2L);
        req.setTitle("Test AD #101 Updated Title");
        req.setDescription("Test AD #101 Updated Description");
        req.setPrice(BigDecimal.valueOf(101.5));
        req.getFields().add(new AdRequestDto.AdFieldDto(1L, "101.5")); // area
        req.getFields().add(new AdRequestDto.AdFieldDto(2L, "2")); // monthly

        // via service
        Ad ad = adRepo.findById(id).orElseThrow();
        adService.updateAd(req, ad);

        Ad updatedAd = adRepo.findById(id).orElseThrow();
        assert updatedAd.getTitle().toLowerCase().contains("updated");
        assert updatedAd.getDescription().toLowerCase().contains("updated");
        assert updatedAd.getPrice().doubleValue() == 101.5;
        assert updatedAd.getFields().size() == 1;
        assert updatedAd.getOptions().size() == 1;
        assert updatedAd.getUpdated() != null;
        assert !updatedAd.getUpdated().isBefore(updatedAd.getUpdated());
    }

    @Test
    void updateAdForSaleSuccessful() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto req = new AdRequestDto();
        req.setCategory(1L);
        req.setTitle("Test AD #102 Updated Title");
        req.setDescription("Test AD #102 Updated Description");
        req.setPrice(BigDecimal.valueOf(50102.5));
        req.getFields().add(new AdRequestDto.AdFieldDto(1L, "102.5")); // area
        req.getFields().add(new AdRequestDto.AdFieldDto(7L, "9")); // feature

        // via controller
        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, req, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("\"title\":");

        Ad updatedAd = adRepo.findById(id).orElseThrow();
        assert updatedAd.getTitle().toLowerCase().contains("updated");
        assert updatedAd.getDescription().toLowerCase().contains("updated");
        assert updatedAd.getPrice().doubleValue() == 50102.5;
        assert updatedAd.getFields().size() == 1;
        assert updatedAd.getOptions().size() == 1;
        assert updatedAd.getUpdated() != null;
        assert !updatedAd.getUpdated().isBefore(updatedAd.getUpdated());
    }

    @Test
    void updateAdForRentWithoutRequiredFields() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto req = new AdRequestDto();
        req.setCategory(2L);
        req.setTitle("Updated");
        req.setPrice(BigDecimal.valueOf(0));
        req.getFields().add(new AdRequestDto.AdFieldDto(2L, null));

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, req, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages"); // json
        assert response.getBody().contains("Missing required field");
    }

    @Test
    void updateAdForRentWithInvalidFields() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto req = new AdRequestDto();
        req.setCategory(1L);
        req.setTitle("Updated");
        req.setPrice(BigDecimal.valueOf(0));
        req.getFields().add(new AdRequestDto.AdFieldDto(3L, "9025")); // year

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, req, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages"); // json
        assert response.getBody().toLowerCase().contains("construction year");
    }

    @Test
    void updateAdForRentWithInvalidFields2() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto req = new AdRequestDto();
        req.setCategory(1L);
        req.setTitle("Updated");
        req.setPrice(BigDecimal.valueOf(0));
        req.getFields().add(new AdRequestDto.AdFieldDto(3L, "ABC")); // year

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, req, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages"); // json
        assert response.getBody().toLowerCase().contains("construction year");
    }

    @Test
    void updateAdForRentWithInvalidFields3() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto req = new AdRequestDto();
        req.setCategory(1L);
        req.setTitle("Updated");
        req.setPrice(BigDecimal.valueOf(0));
        req.getFields().add(new AdRequestDto.AdFieldDto(4L, "INVALID-URL"));

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, req, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages"); // json
        assert response.getBody().toLowerCase().contains("url is incorrect");
    }

    @Test
    void updateAdForRentWithInvalidFields4() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        AdRequestDto req = new AdRequestDto();
        req.setCategory(1L);
        req.setTitle("Updated");
        req.setPrice(BigDecimal.valueOf(0));
        req.getFields().add(new AdRequestDto.AdFieldDto(3L, "1025")); // year
        req.getFields().add(new AdRequestDto.AdFieldDto(3L, "9025")); // year

        String token = retrieveAdminToken();
        ResponseEntity<String> response = apiRequestPost(
                "/api/ads/" + id, req, token, String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("messages"); // json
        assert response.getBody().toLowerCase().contains("multiple answers");
    }
}
