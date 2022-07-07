package com.musicloud.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class StorageService {
    private final Cloudinary cloudinary;

    public StorageService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dtzjbyjzq",
                "api_key", "559841441855544",
                "api_secret", "MHDYLbfMmCfHikjiQKJZSZs0ZD0",
                "secure", true));
    }

    public String saveImage(MultipartFile image) throws IOException {
        File file = resizeImage(image, 200, 200);
        String fileType = Files.probeContentType(file.toPath());
        long fileSize = Files.size(file.toPath());

        String url = null;
        if (fileType != null && (fileType.equals("image/jpeg") || fileType.equals("image/png")) && fileSize < 200000) {
            Map upload = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            url = (String) upload.get("url");
        }
        file.delete();
        return url;
    }

    private File convertMultiPartToFile(BufferedImage bufferedImage, String fileName) throws IOException {
        File outputFile = new File(fileName);
        ImageIO.write(bufferedImage, "jpg", outputFile);
        return outputFile;
    }

    private File resizeImage(MultipartFile file, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(ImageIO.read(file.getInputStream()), 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return convertMultiPartToFile(resizedImage, file.getOriginalFilename());
    }

}
