package com.musicloud.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

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

    //todo:implement cloudinary
    public String saveImage(MultipartFile image) throws IOException {
        File file = convertMultiPartToFile(image);
        Map upload = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        file.delete();

        return (String) upload.get("url");
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
