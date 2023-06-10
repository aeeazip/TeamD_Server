package com.presenty.backend.service;

import com.presenty.backend.domain.image.Image;
import com.presenty.backend.domain.image.repository.ImageRepository;
import com.presenty.backend.error.exception.EntityNotFoundException;
import com.presenty.backend.service.dto.CreateResult;
import com.presenty.backend.service.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final ImageStore imageStore;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
    private final ImageRepository imageRepository;

    @Transactional
    public CreateResult<Long> saveImage(Resource imageResource) {
        String originName = imageResource.getFilename();
        String storedName = createStoredName(originName);
        String url = imageStore.store(imageResource, storedName);
        Image image = imageRepository.save(Image.builder()
                .originName(imageResource.getFilename())
                .storedName(createStoredName(originName))
                .url(url)
                .build());
        return new CreateResult<>(image.getId());
    }

    public ImageDto get(Long imageId) {
        Image image = getImageById(imageId);
        return ImageDto.builder()
                .imageId(image.getId())
                .originName(image.getOriginName())
                .storedName(image.getStoredName())
                .url(image.getUrl())
                .build();
    }

    private String createStoredName(String originalName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalName);
        String now = LocalDateTime.now().format(dateTimeFormatter);
        return now + "/" + uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public boolean isNotOwnedByMember(Long memberId, Long imageId) {
        Image image = getImageById(imageId);
        return !Objects.equals(image.getCreatedBy(), memberId);
    }

    private Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image id=" + imageId));
    }

}
