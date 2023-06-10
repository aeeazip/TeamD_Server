package com.presenty.backend.service;

import com.presenty.backend.error.exception.InvalidImageException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class LocalImageStore implements ImageStore {

    public final File imageDir;

    public LocalImageStore(@Value("${dir.image}") String dir) {
        imageDir = new File(dir);
    }

    @PostConstruct
    public void init() {
        if (!imageDir.exists() && !imageDir.mkdirs()) {
            throw new IllegalStateException("이미지 저장을 위한 로컬 디렉토리 생성에 실패했습니다.");
        }
    }

    @Override
    public String store(Resource image, String storedName) {
        File imagePath = new File(imageDir, storedName);

        try (InputStream in = image.getInputStream(); OutputStream out = new FileOutputStream(imagePath)) {
            out.write(in.readAllBytes());
            return imagePath.toString();
        } catch (IOException e) {
            throw new InvalidImageException(e);
        }
    }
}
