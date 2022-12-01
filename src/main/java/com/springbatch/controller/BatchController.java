package com.springbatch.controller;

import com.springbatch.util.Account;
import com.springbatch.util.JobNames;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.springbatch.util.JobNames.chunkJob;

@RequiredArgsConstructor
@RestController
public class BatchController extends Account {

    private final JobOperator jobOperator;
    private final JobRegistry jobRegistry;


    @GetMapping(value = "/batch1")
    public String batch1(@RequestParam String param)
            throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {

        jobOperator.start(chunkJob.name(), "todate=" + LocalDateTime.now().toString());

        return "success";
    }


    @GetMapping(value = "/batch2")
    public String batch2()
            throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {

        jobOperator.start(chunkJob.name(), "todate=" + LocalDateTime.now().toString());

        return "success";
    }

}
