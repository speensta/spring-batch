package com.springbatch.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobScheduler {

    private final Job chunkJob;
    private final JobLauncher jobLauncher;

    @Scheduled(fixedDelay = 60 * 1000L)
    public void execute()
            throws JobInstanceAlreadyCompleteException,
                   JobExecutionAlreadyRunningException,
                   JobParametersInvalidException,
                   JobRestartException {
        log.info("********************** JobScheduler start ********************** ");
        jobLauncher.run(chunkJob, new JobParametersBuilder()
                    .addString("todate", LocalDateTime.now().toString()).toJobParameters()
        );
        log.info("********************** JobScheduler end ********************** ");
    }
}
