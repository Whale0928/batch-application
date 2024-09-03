package app.batch;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig extends DefaultBatchConfiguration {

    // 로그를 남기기 위한 Logger 설정
    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    private final BatchStepProcess batchStepProcess;

    /**
     * JobRegistryBeanPostProcessor를 설정하는 메서드.
     * Spring Batch에서 JobRegistry는 모든 Job을 관리하고 추적하는 역할을 합니다.
     * JobRegistryBeanPostProcessor는 모든 Job을 JobRegistry에 등록하는 역할을 합니다.
     *
     * @param jobRegistry - Spring Batch에서 Job을 관리하는 레지스트리
     * @return JobRegistryBeanPostProcessor - JobRegistry를 관리하는 빈
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    /**
     * 실제로 배치 작업(Job)을 정의하는 메서드.
     * 여기서 "simple"이라는 이름의 Job을 정의하고, 각 Step의 성공과 실패에 따라 흐름을 제어합니다.
     *
     * @param jobRepository      - Job의 메타데이터를 저장하고 관리하는 JPA Repository
     * @param transactionManager - 트랜잭션을 관리하는 매니저
     * @return Job - 정의된 배치 작업
     */
    @Bean
    public Job testJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("simple", jobRepository)
                .start(batchStepProcess.testStep(jobRepository, transactionManager))
                // Step이 성공적으로 완료되었을 때는 Job을 종료
                .on(ExitStatus.COMPLETED.getExitCode()).end()
                // Step이 실패했을 때는 failedStep으로 이동
                .from(batchStepProcess.testStep(jobRepository, transactionManager))
                .on(ExitStatus.FAILED.getExitCode()).to(batchStepProcess.failedStep(jobRepository, transactionManager))
                .end()
                .build();
    }

}