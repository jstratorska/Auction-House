package com.aubgteam.auctionhouse.Services;

import com.aubgteam.auctionhouse.Models.Image;
import com.aubgteam.auctionhouse.Repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image save(MultipartFile imagePath) throws IOException  {
        String fileName = StringUtils.cleanPath(imagePath.getOriginalFilename());
            Image image = new Image(fileName, imagePath.getContentType(), Base64.getEncoder().encodeToString(imagePath.getBytes()));
            return imageRepository.save(image);
    }

    public Image get(long fileId) {
        return imageRepository.findById(fileId).orElse(null);
    }

    public void delete(Long id)
    {
        imageRepository.deleteById(id);
    }

}

