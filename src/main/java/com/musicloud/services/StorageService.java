package com.musicloud.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class StorageService {
    private final Cloudinary cloudinary;
    public static final long MAX_IMAGE_SIZE = 200000;
    public static final long MAX_SONG_SIZE = 20000000;

    public StorageService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dtzjbyjzq",
                "api_key", "559841441855544",
                "api_secret", "MHDYLbfMmCfHikjiQKJZSZs0ZD0",
                "secure", true));
    }

    public String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) return null;

        File file = resizeImage(image, 200, 200);
        String fileType = Files.probeContentType(file.toPath());
        long fileSize = Files.size(file.toPath());

        String url = null;
        if (fileType != null && (fileType.equals("image/jpeg") || fileType.equals("image/png")) && fileSize < MAX_IMAGE_SIZE) {
            Map upload = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "avatars"));
            url = (String) upload.get("url");
        }
        file.delete();
        return url;
    }

    private File convertBufferedImageToFile(BufferedImage bufferedImage, String fileName) throws IOException {
        File outputFile = new File(fileName);
        ImageIO.write(bufferedImage, "jpg", outputFile);
        return outputFile;
    }

    private File resizeImage(MultipartFile file, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(ImageIO.read(file.getInputStream()), 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return convertBufferedImageToFile(resizedImage, file.getOriginalFilename());
    }

    public String saveSong(MultipartFile songFile) throws IOException {
        File file = convertMultipartFileToFile(songFile);
        String fileType = Files.probeContentType(file.toPath());
        long fileSize = Files.size(file.toPath());

        String url = null;
        if (fileType != null && fileType.equals("audio/mpeg") && fileSize < MAX_SONG_SIZE) {
            Map upload = cloudinary.uploader().upload(file,
                    ObjectUtils.asMap("resource_type", "video",
                            "folder", "songs"));
            url = (String) upload.get("url");
        }
        file.delete();
        return url;
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
