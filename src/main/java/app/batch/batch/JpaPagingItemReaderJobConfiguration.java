package app.batch.batch;

import app.batch.appilication.ImageCommandProcess;
import app.batch.domain.Alcohol;
import app.batch.domain.Mapping;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration extends DefaultBatchConfiguration {
    private final int CHUNK_SIZE = 10;
    private final EntityManagerFactory entityManagerFactory;
    private final ImageCommandProcess imageCommandProcess;
    private final ApplicationEventPublisher publisher;


    @Bean
    public Job jpaPagingItemReaderJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        long startAt = System.nanoTime();
        Job jpaPagingItemReaderJob = new JobBuilder("jpaPagingItemReaderJob", jobRepository)
                .start(jpaPagingItemReaderStep(jobRepository, transactionManager))
                .build();

        long endAt = System.nanoTime();

        log.info("JpaPagingItemReaderJobConfiguration 실행 시간(ms) : {}", (endAt - startAt) / 1000000);
        return jpaPagingItemReaderJob;
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
        return new JpaPagingItemReaderBuilder<Alcohol>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT p FROM alcohol p order by p.id")
                .build();
    }

    private ItemWriter<Alcohol> jpaPagingItemWriter() {
        return list -> {
            for (Alcohol alcohol : list) {
                log.info("write thread name : {}, alcohol id : {}", Thread.currentThread().getName(), alcohol.getId());
                Mapping mapping = imageCommandProcess.downloadImage(alcohol.getId(), alcohol.getImageUrl());
                publisher.publishEvent(mapping);
            }
        };
    }
}
