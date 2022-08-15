package com.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final long currentMilliseconds = new Date().getTime();

    @Bean
    public Job testjob() {
        return jobBuilderFactory.get("testjob"+currentMilliseconds)
                .incrementer(new RunIdIncrementer())
                .start(batchStep1())
                .next(batchStep2())
                .next(batchStep3())
                .next(batchChunkStep())
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
                .allowStartIfComplete(true)
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
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step batchStep3() {
        return stepBuilderFactory.get("batchStep3")
                .tasklet(new CustomTasklet())
                .allowStartIfComplete(true)
                .build();
    }


    @Bean
    public Step batchChunkStep() {
        return stepBuilderFactory.get("batchChunkStep").<String, String>chunk(10)
                .reader(new ListItemReader<>(Arrays.asList("item1","item2","item3","item4","item5","item6","item7")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String s) throws Exception {
                        return s.toUpperCase();
                    }
                })
                .writer(new ItemWriter<Object>() {
                    @Override
                    public void write(List<?> list) throws Exception {
                        list.forEach(item -> log.info("************chunkStepWrite***"+item));
                    }
                })
                .build();
    }


}
