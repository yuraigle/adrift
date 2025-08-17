package ru.orlov.adrift;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.Ad;
import ru.orlov.adrift.domain.AdField;
import ru.orlov.adrift.domain.AdRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdCreatingTests extends AbstractTest {

    @Autowired
    AdRepository adRepository;

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void createAdUnauthorized() {
        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #0");
        form.setCategory(1L);
        ResponseEntity<String> response = apiRequestPost("/api/ads", form, null, String.class);

        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void createAdMalformed() {
        AdRequestDto form = new AdRequestDto();
        form.setTitle(null);
        form.setCategory(0L);

        String token = retrieveToken();
        ResponseEntity<String> response = apiRequestPost("/api/ads", form, token, String.class);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().contains("title");
        assert response.getBody().contains("category");
    }

    @Test
    void createAdSuccessContainsCreatedId() {
        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #0");
        form.setDescription("Test description");
        form.setCategory(1L);

        String token = retrieveToken();
        ResponseEntity<String> resp = apiRequestPost("/api/ads", form, token, String.class);

        assert resp.getStatusCode() == HttpStatus.CREATED;
        assert resp.getBody() != null;
        assert resp.getBody().contains("\"id\":");

        adRepository.deleteAll(adRepository.findAllByTitleLike("Test AD %"));
    }

    @Test
    void createAdWithCustomFields() {
        adRepository.deleteAll(adRepository.findAllByTitleLike("Test AD %"));

        // create an Ad using rest api
        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #1");
        form.setDescription("Test description");
        form.setCategory(1L); // Houses for sale
        form.setFields(List.of(
                new AdRequestDto.AdFieldDto(1L, "67.60"), // area
                new AdRequestDto.AdFieldDto(3L, "1985"), // construction year
                new AdRequestDto.AdFieldDto(4L, "https://example.com") // www
        ));

        String token = retrieveToken();
        ResponseEntity<String> resp = apiRequestPost(
                "/api/ads", form, token, String.class
        );

        // assert Ad is created
        assert resp.getStatusCode() == HttpStatus.CREATED;
        assert resp.getBody() != null;

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

        // cleanup: delete test Ads
        adRepository.deleteAll(adRepository.findAllByTitleLike("Test AD %"));

        // assert Ad Fields are deleted too
        String sql1 = """
                select count(*) as cnt from ads_fields
                where ad_id is null or question_id is null
                """;
        int cnt = (Integer) jdbc.queryForMap(sql1).get("cnt");
        assert cnt == 0;
    }

}
