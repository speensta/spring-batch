package com.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job testjob(Step batchStep1, Step batchStep2) {
        return jobBuilderFactory.get("testjob")
                .start(batchStep1)
                .next(batchStep2)
                .build();
    }

    @Bean
    public Step batchStep1() {
        return stepBuilderFactory.get("batchStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        log.info("**************************************");
                        log.info("**************** step1 ***************");
                        log.info("**************************************");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step batchStep2() {
        return stepBuilderFactory.get("batchStep2")
                .tasklet((configuration, chunkContext) -> {
                    log.info("**************************************");
                    log.info("**************** step2 ***************");
                    log.info("**************************************");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }





}
