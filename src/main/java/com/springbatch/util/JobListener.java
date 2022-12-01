package com.springbatch.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("************** JobListener beforeJob ************** ");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("************** JobListener afterJob ************** ");
    }
}
