package app.batch.batch;

import app.batch.appilication.ImageCommandProcess;
import app.batch.domain.Alcohol;
import app.batch.repository.JpaAlcoholRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BatchStepProcess {

    private static final Logger log = LogManager.getLogger(BatchStepProcess.class);
    private final JpaAlcoholRepository alcoholRepository;
    private final ImageCommandProcess imageCommandProcess;

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

                    List<Alcohol> alcohols = alcoholRepository.findAll();

                    if (alcohols.isEmpty()) {
                        contribution.setExitStatus(ExitStatus.FAILED);
                    }
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
