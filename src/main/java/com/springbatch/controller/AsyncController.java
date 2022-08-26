package com.springbatch.controller;

import com.springbatch.service.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@RestController
public class AsyncController {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public final DataSource dataSource;

    public final PagingQueryProvider createQueryProvider;

    private final AsyncService asyncService;

    @GetMapping("/async")
    public String async() throws InterruptedException {

        asyncService.asyncTest();

        return "success";
    }

    @GetMapping("/async2")
    public String async2() throws InterruptedException, ExecutionException {

        Future<String> future = asyncService.asyncMethodWithReturnType();

        while (true) {
            if (future.isDone()) {
                System.out.println("Result from asynchronous process - " + future.get());
                return "Result from asynchronous process";
            }
            System.out.println("Continue doing something else. ");
            Thread.sleep(1000);
        }

    }

}
