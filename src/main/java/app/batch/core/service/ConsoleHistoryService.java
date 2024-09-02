package app.batch.core.service;

import app.batch.core.entity.ConsoleHistory;
import app.batch.core.entity.ConsoleHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsoleHistoryService {

    private final ConsoleHistoryRepository consoleHistoryRepository;

    public ConsoleHistoryService(ConsoleHistoryRepository consoleHistoryRepository) {
        this.consoleHistoryRepository = consoleHistoryRepository;
    }

    public void eventPublish(StepContribution contribution, ChunkContext chunkContext) {

        final String jobName = chunkContext.getStepContext().getJobName();
        final Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();

        final String job = "jobName : " + jobName + ", jobExecutionId : " + jobExecutionId;

        final String stepName = chunkContext.getStepContext().getStepName();
        final Long stepExecutionId = chunkContext.getStepContext().getStepExecution().getId();

        final String step = "stepName : " + stepName + ", stepExecutionId : " + stepExecutionId;

        ConsoleHistory history = consoleHistoryRepository.save(ConsoleHistory.of(job, step));

        log.info("save history: {}", history);
    }
}
