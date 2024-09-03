package app.batch;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static app.batch.BatchStepProcess.downloadImage;


class BatchStepProcessTest {

    @Test
    void imageDown() {
        long startAt = System.nanoTime();
        String imageUrl = "https://static.whiskybase.com/storage/whiskies/1/8/3881/318643-big.jpg";
        String destinationFile = UUID.randomUUID() + ".png";

        try {
            downloadImage(imageUrl, destinationFile);
            System.out.println("이미지가 성공적으로 다운로드되었습니다.: " + destinationFile);
        } catch (IOException e) {
            System.out.println("이미지를 다운로드하지 못했습니다.: " + e.getMessage());
        }

        long endAt = System.nanoTime();
        System.out.println("이미지 다운로드에 걸린 시간: " + (endAt - startAt) / 1000000 + "ms");
    }
}