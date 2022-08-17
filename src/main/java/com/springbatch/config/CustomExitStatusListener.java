package com.springbatch.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CustomExitStatusListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        String exitcode = stepExecution.getExitStatus().getExitCode();
        if(!exitcode.equals(ExitStatus.FAILED.getExitCode())) {
            return new ExitStatus("PASS");
        }

        return null;
    }
}
