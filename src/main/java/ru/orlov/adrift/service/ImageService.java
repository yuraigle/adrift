package ru.orlov.adrift.service;

import dev.matrixlab.webp4j.WebPCodec;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.orlov.adrift.domain.Ad;
import ru.orlov.adrift.domain.AdImage;
import ru.orlov.adrift.domain.AdImageRepository;
import ru.orlov.adrift.domain.ex.AppException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AdImageRepository imageRepository;
    private final StorageService storageService;
    List<String> allowedTypes = List.of("image/jpeg", "image/jpg", "image/png", "image/webp");

    public AdImage uploadAdImage(Ad ad, MultipartFile file) throws AppException {
        AdImage image = new AdImage();

        image.setAd(ad);
        image.setFilename(generateFileName());
        image.setOrigFilename(file.getOriginalFilename());

        convertAndSaveImage(file, image.getFilename(), 1280, 960);
        convertAndSaveImage(file, image.getFilename(), 800, 600);
        convertAndSaveImage(file, image.getFilename(), 400, 300);

        // todo generate alt text

        return imageRepository.save(image);
    }

    private String generateFileName() {
        return RandomStringUtils.secure()
                .nextAlphanumeric(16)
                .toLowerCase();
    }

    private void convertAndSaveImage(MultipartFile file, String filename, int w, int h) throws AppException {
        BufferedImage image;
        try {
            image = readImage(file);

            if (image == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AppException("Failed to read image", 400);
        }

        byte[] resizedImage;
        try {
            resizedImage = resizeJpeg(image, w, h);

            if (resizedImage.length == 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AppException("Failed to make thumbnail", 400);
        }

        byte[] webpImage;
        try {
            webpImage = convertToWebp(resizedImage);

            if (webpImage.length == 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AppException("Failed to convert to webp", 400);
        }

        try {
            storageService.uploadFileToS3(filename + "_" + h + ".webp", webpImage);
        } catch (Exception e) {
            throw new AppException("Failed to upload file to storage", 400);
        }
    }

    private BufferedImage readImage(MultipartFile file) throws AppException {
        String type = file.getContentType();
        if (type == null || !allowedTypes.contains(type)) {
            throw new AppException("Unsupported image type", 400);
        }

        if (type.equalsIgnoreCase("image/webp")) {
            try {
                byte[] bytes = file.getInputStream().readAllBytes();
                return WebPCodec.decodeImage(bytes);
            } catch (IOException e) {
                throw new AppException("Failed to read webp image", 400);
            }
        }

        try {
            return ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new AppException("Failed to read image", 400);
        }
    }

    private byte[] resizeJpeg(BufferedImage image, int w, int h) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(image)
                .size(w, h)
                .outputFormat("jpeg")
                .outputQuality(1)
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    private byte[] convertToWebp(byte[] bytesJpeg) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytesJpeg));

        return WebPCodec.encodeImage(image, 80.0f);
    }

}
