package app.batch;

import app.batch.domain.ImageHistory;
import app.batch.repository.JpaAlcoholRepository;
import app.batch.repository.JpaImageHistoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Component
public class BatchStepProcess {

    private static final Logger log = LogManager.getLogger(BatchStepProcess.class);
    private final JpaAlcoholRepository alcoholRepository;
    private final JpaImageHistoryRepository imageHistoryRepository;

    public BatchStepProcess(JpaAlcoholRepository alcoholRepository, JpaImageHistoryRepository imageHistoryRepository) {
        this.alcoholRepository = alcoholRepository;
        this.imageHistoryRepository = imageHistoryRepository;
    }

    public static void downloadImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream out = new FileOutputStream(destinationFile)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        }
    }

    /**
     * testStep은 실제로 비즈니스 로직이 실행되는 부분입니다.
     * JpaAlcoholRepository를 통해 이미지 데이터를 조회하고, 이를 기반으로 ImageHistory 엔티티를 생성하고 저장합니다.
     * 저장이 성공하면 성공 상태로, 실패하면 실패 상태로 Job의 상태를 설정합니다.
     *
     * @param jobRepository      - Job의 메타데이터를 저장하고 관리하는 JPA Repository
     * @param transactionManager - 트랜잭션을 관리하는 매니저
     * @return Step - 정의된 Step
     */
    public Step testStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {


                    // JpaAlcoholRepository를 통해 모든 이미지 URL 데이터를 가져옴
                    List<String> images = alcoholRepository.findAllImages();

                    // 가져온 이미지 개수를 바탕으로 ImageHistory 엔티티 생성
                    ImageHistory imageHistory = ImageHistory.create((long) images.size());

                    // ImageHistory 엔티티를 데이터베이스에 저장
                    ImageHistory save = imageHistoryRepository.save(imageHistory);

                    // 저장된 엔티티의 ID가 null이면 실패로 간주하고 ExitStatus를 FAILED로 설정
                    if (save.getId() == null) {
                        contribution.setExitStatus(ExitStatus.FAILED);
                    } else {
                        // 성공적으로 저장되었다면 ExitStatus를 COMPLETED로 설정
                        contribution.setExitStatus(ExitStatus.COMPLETED);
                    }

                    // Tasklet이 정상적으로 종료되었음을 나타냄
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    /**
     * failedStep은 testStep이 실패했을 때 실행되는 보정 단계입니다.
     * 여기서는 단순히 실패 로그를 남기는 작업만 수행합니다.
     *
     * @param jobRepository      - Job의 메타데이터를 저장하고 관리하는 JPA Repository
     * @param transactionManager - 트랜잭션을 관리하는 매니저
     * @return Step - 정의된 실패 Step
     */
    public Step failedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("failedStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // 실패 시 로그에 메시지를 남김
                    log.info("Failed Step");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
