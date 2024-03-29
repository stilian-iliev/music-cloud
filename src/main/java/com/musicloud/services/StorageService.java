package com.musicloud.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.musicloud.models.dtos.cloudinary.CloudinaryEntry;
import com.musicloud.models.dtos.cloudinary.CloudinaryResponse;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageService {
    private final Cloudinary cloudinary;
    private final ModelMapper mapper;
    public static final long MAX_IMAGE_SIZE = 200000;
    public static final long MAX_SONG_SIZE = 20000000;

    public StorageService(ModelMapper mapper,
                          @Value("${cloudinary.name}") String cloudName,
                          @Value("${cloudinary.key}") String cloudKey,
                          @Value("${cloudinary.secret}") String cloudSecret
    ) {
        this.mapper = mapper;
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", cloudKey,
                "api_secret", cloudSecret,
                "secure", true));
    }

    public String saveImage(MultipartFile image, String folder) throws IOException {
        if (image == null || image.isEmpty()) return null;

        File file = resizeImage(image, 400, 400);
        String fileType = Files.probeContentType(file.toPath());
        long fileSize = Files.size(file.toPath());

        String url = null;
        if (fileType != null && (fileType.equals("image/jpeg") || fileType.equals("image/png")) && fileSize < MAX_IMAGE_SIZE) {
            Map upload = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", folder));
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

    public Map<String, String > saveSong(MultipartFile songFile) throws IOException {
        File file = convertMultipartFileToFile(songFile);
        String fileType = Files.probeContentType(file.toPath());
        long fileSize = Files.size(file.toPath());

        String url = null;
        int duration = 0;
        if (fileType != null && fileType.equals("audio/mpeg") && fileSize < MAX_SONG_SIZE) {
            Map upload = cloudinary.uploader().upload(file,
                    ObjectUtils.asMap("resource_type", "video",
                            "folder", "songs"));
            url = (String) upload.get("url");
            try {
                AudioFile audioFile = AudioFileIO.read(file);
                duration = audioFile.getAudioHeader().getTrackLength();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        file.delete();
        Map<String ,String > output = new HashMap<>();
        output.put("url", url);
        output.put("duration", String.valueOf(duration));
        return output;
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public void deleteUnusedFilesFromFolder(String folderName, List<String> activeFilesUrls) throws Exception {
        ApiResponse response = cloudinary.search().expression(String.format("folder:%s/*", folderName)).execute();
        CloudinaryResponse resources = mapper.map(response, CloudinaryResponse.class);
        for (CloudinaryEntry resource : resources.getResources()) {
            if (!activeFilesUrls.contains(resource.getUrl())) {
                cloudinary.uploader().destroy(resource.getPublic_id(), ObjectUtils.asMap("resource_type", resource.getResource_type()));
            }
        }
    }
}
