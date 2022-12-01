package com.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchExitStatusConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final long currentMilliseconds = new Date().getTime();

//    @Bean
//    public Job batchExitSatusJob() {
//
//        return jobBuilderFactory.get("exitStatusJob"+currentMilliseconds)
//                .incrementer(new RunIdIncrementer())
//                .start(exitStatusStep1())
//                    .on("FAILED")
//                    .to(exitStatusStep2())
//                    .on("PASS")
//                    .stop()
//                .end()
//                .build();
//
//    }

    @Bean
    public Step exitStatusStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet((stepContribution, chunkContext) -> {
                        log.info("********************* exitStatusStep1");
                        stepContribution.getStepExecution().setExitStatus(ExitStatus.FAILED);
                        return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step exitStatusStep2() {
        return stepBuilderFactory.get("step1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("********************* exitStatusStep2");
                    return RepeatStatus.FINISHED;
                })
                .listener(new CustomExitStatusListener())
                .build();
    }
}
