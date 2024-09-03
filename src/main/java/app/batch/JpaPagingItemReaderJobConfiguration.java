package app.batch;

import app.batch.domain.Alcohol;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration extends DefaultBatchConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final int CHUNK_SIZE = 100;

    @Bean
    public Job jpaPagingItemReaderJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info(" Job 시작  ");
        return new JobBuilder("jpaPagingItemReaderJob", jobRepository)
                .start(jpaPagingItemReaderStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step jpaPagingItemReaderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        log.info(" Step 시작  ");

        return new StepBuilder("jpaPagingItemReaderStep", jobRepository)
                .<Alcohol, Alcohol>chunk(CHUNK_SIZE, transactionManager)
                .reader(jpaPagingItemReader())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Alcohol> jpaPagingItemReader() {

        log.info("페이징 리더 시작");

        return new JpaPagingItemReaderBuilder<Alcohol>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT p FROM alcohol p order by p.id")
                .build();
    }

    private ItemWriter<Alcohol> jpaPagingItemWriter() {

        log.info("페이징 라이터 시작");

        return list -> {
            for (Alcohol alcohol : list) {
                log.info("Current Alcohol={}", alcohol.getId());
            }
        };
    }
}