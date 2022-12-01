package com.springbatch.controller;

import com.springbatch.domain.Member;
import com.springbatch.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AsyncController {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public final DataSource dataSource;

//    public final PagingQueryProvider createQueryProvider;

    private final AsyncService asyncService;

    @GetMapping("/async")
    public String async() throws InterruptedException {
        asyncService.asyncTest();
        return "success";
    }


    @Async("asyncExecutor")
    @GetMapping("/async2")
    public String async2() {
        Future<String> future = asyncService.asyncMethodWithReturnType();
        return asyncService.isDone(future);
    }


    @Async("asyncExecutor")
    @GetMapping("/async3")
    public CompletableFuture<?> async3() throws InterruptedException {
//        Future<String> future = asyncService.asyncMethodWithReturnType();

        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            try {
                asyncService.asyncTest();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "success supplyAsync";
        }).thenApply(s -> {
            log.info("============================="+s);
            return s;
        });

        return future;
//        return CompletableFuture.completedFuture(asyncService.asyncTest());
    }


    @GetMapping("/synctest")
    public String synctest() throws InterruptedException {

        CompletableFuture<List<Member>> completableFuture = asyncService.asyncTest2();
        completableFuture.thenAccept(list -> {
            List t = list;
            t.forEach(o -> System.out.println("=================="+o));
        });

        return "processing";
    }

}
