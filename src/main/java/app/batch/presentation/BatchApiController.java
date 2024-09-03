package app.batch.presentation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class BatchApiController {

    private static final Logger log = LogManager.getLogger(BatchApiController.class);
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @GetMapping("/run")
    public ResponseEntity<?> runJob() throws Exception {
        String time = LocalDateTime.now().toString();

        Job job = jobRegistry.getJob("jpaPagingItemReaderJob"); // job 이름
        JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);

        log.info("Job 실행 시작");
        jobLauncher.run(job, jobParam.toJobParameters());
        log.info("Job 실행 완료");

        return ResponseEntity.ok("Job 실행 완료");
    }

}
