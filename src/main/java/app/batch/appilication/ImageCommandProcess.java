package app.batch.appilication;

import app.batch.domain.Mapping;
import app.batch.repository.JpaAlcoholImageMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageCommandProcess {

    private final JpaAlcoholImageMappingRepository alcoholImageMappingRepository;

    @Async
    public Mapping downloadImage(Long alcoholsId, String imageUrl) throws IOException {

        log.info("image download thread name : {}", Thread.currentThread().getName());

        String newName = UUID.randomUUID() + ".jpg";
        String directoryPath = "images";
        String filePath = directoryPath + File.separator + newName;
        URL url = new URL(imageUrl);


        try (InputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream out = new FileOutputStream(filePath)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        }

        String cdnUrl = uploadImage(filePath);

        return Mapping.builder()
                .alcoholId(alcoholsId)
                .imageUrl(imageUrl)
                .cdnUrl(cdnUrl)
                .build();
    }

    public String uploadImage(String filePath) {

        //s3 에 이미지 업로드
        return null;
    }
}
