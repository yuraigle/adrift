package ru.orlov.adrift.initializr;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.orlov.adrift.domain.Ad;
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.ImageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
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
            String bg = String.format("%06x", random.nextInt(0x1000000));
            String fg = String.format("%06x", random.nextInt(0x1000000));

            String text = StringUtils.right(ad.getTitle(), 25);
            text = text.replaceAll("^.*? ", "");
            String textUrl = StringUtils.replace(text, " ", "+");
            String url = String.format("https://placehold.co/%dx%d/%s/%s.jpg?text=%s",
                    w, h, bg, fg, textUrl);

            log.info("Downloading image file {}", url);
            try (InputStream in = URI.create(url).toURL().openStream()) {
                BufferedImage image = ImageIO.read(in);
                imageService.uploadAdImage(ad, image, "file.jpg");
            } catch (IOException | AppException e) {
                log.error("Error loading image", e);
            }
        }
    }

}
