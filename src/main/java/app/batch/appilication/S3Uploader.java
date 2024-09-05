package app.batch.appilication;

import app.batch.domain.Mapping;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
public class S3Uploader {

    private final AmazonS3 amazonS3Client;
    @Value("${aws.bucket}")
    private String bucket;

    public Mapping downloadImage(Long alcoholsId, String imageUrl) throws IOException {
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

        File file = new File(filePath);
        String upload = upload(file, "alcohols/whisky");
        return Mapping.builder()
                .alcoholId(alcoholsId)
                .imageUrl(imageUrl)
                .cdnUrl(upload)
                .build();
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        return putS3(uploadFile, fileName);
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.Private)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
