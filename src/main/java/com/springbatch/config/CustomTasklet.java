package com.springbatch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class CustomTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String stepName = stepContribution.getStepExecution().getStepName();
        String jobName = chunkContext.getStepContext().getJobName();
        log.info("******************* customTasklet stepName **"+stepName);
        log.info("******************* customTasklet jobName **"+jobName);
        return RepeatStatus.FINISHED;
    }
}
