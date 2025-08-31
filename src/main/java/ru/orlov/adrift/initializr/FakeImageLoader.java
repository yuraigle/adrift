package ru.orlov.adrift.initializr;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.domain.Ad;
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.ImageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor
public class FakeImageLoader {

    private final AdRepository adRepository;
    private final ImageService imageService;

    public void createFakeImages() {
        List<Ad> ads = adRepository.findAll();

        Random random = new Random();
        for (Ad ad : ads) {
            boolean hasImage = random.nextInt(10) > 3;
            if (!hasImage) {
                continue;
            }

            int h = random.nextInt(1000) + 600;
            int w = random.nextInt(1000) + 600;
            String url = String.format("https://placebeard.it/%d/%d", w, h);

            log.info("Downloading image file {}", url);
            try (InputStream in = URI.create(url).toURL().openStream()) {
                BufferedImage image = ImageIO.read(in);
                imageService.uploadAdImage(ad, image, "beard.jpg");
            } catch (IOException | AppException e) {
                log.error("Error loading image", e);
            }
        }
    }

}
