package com.springbatch.controller;

import com.nexacro.uiadapter17.spring.core.annotation.ParamDataSet;
import com.nexacro.uiadapter17.spring.core.data.NexacroResult;
import com.springbatch.domain.Member;
import com.springbatch.service.AsyncService;
import com.springbatch.service.NexacroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NexacroController {

    private final JobOperator jobOperator;
    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;
    private final NexacroService nexacroService;
    private final AsyncService asyncService;
    private final JobLauncher jobLauncher;


    @GetMapping("/nexacro/list")
    public List<Member> findByAll() {
        List<Member> result = nexacroService.findByAll();
        return result;
    }

    @GetMapping("/list")
    public CompletableFuture<NexacroResult> findByAll2() throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {
//        Future<String> future = asyncService.asyncMethodWithReturnType();
//        String result = asyncService.isDone(future);
        String result = "";

//        if(future.isDone()) {
//            log.info("=====================result="+result);
//        }

        JobParameters jobParameter = new JobParametersBuilder()
                .addString("testcode", "K2")
                .toJobParameters();

//        jobRegistry.getJobNames().forEach(name -> {
//            try {
//                jobOperator.start(name, "testcode=K2");
//            } catch (NoSuchJobException e) {
//                throw new RuntimeException(e);
//            } catch (JobInstanceAlreadyExistsException e) {
//                throw new RuntimeException(e);
//            } catch (JobParametersInvalidException e) {
//                throw new RuntimeException(e);
//            }
//        });

//        jobOperator.start("jobChunk", "testcode=K2");

        CompletableFuture<NexacroResult> future = CompletableFuture.supplyAsync(() -> {
            try {
                asyncService.asyncTest();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "success supplyAsync";
        }).thenApply(s -> {
            log.info("============================="+s);
            NexacroResult nexacroResult = new NexacroResult();
            List<Member> resultList = nexacroService.findByAll();
            nexacroResult.addDataSet("ds_list", resultList);
            nexacroResult.addVariable("result", s);
            return nexacroResult;
        });

        NexacroResult nexacroResult = new NexacroResult();
        List<Member> resultList = nexacroService.findByAll();
        nexacroResult.addDataSet("ds_list", resultList);
        nexacroResult.addVariable("result", "success");
        try {
            asyncService.asyncTest();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new AsyncResult<>(nexacroResult).completable();
    }

    @PostMapping("/create")
    public void create(@ParamDataSet(name = "ds_input") Member member) {
        nexacroService.create(member);
    }

    @PostMapping("/update")
    public void update(@ParamDataSet(name = "ds_input") Member member) {
        nexacroService.update(member);
    }

    @PostMapping("/delete")
    public void delete(@ParamDataSet(name = "ds_input") Member member) {
        nexacroService.delete(member);
    }



}
