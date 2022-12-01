package com.springbatch.service;

import com.springbatch.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsyncService {

    private final NexacroService nexacroService;

    public String asyncTest() throws InterruptedException {

        Thread.sleep(30000);

        nexacroService.findByAll().stream().forEach(m -> log.info("====================asyncTest="+m.toString()));

        return "success";

    }

    @Async
    public CompletableFuture<List<Member>> asyncTest2() throws InterruptedException {

        Thread.sleep(10000);

        List<Member> list = nexacroService.findByAll();

        return new AsyncResult<List<Member>>(list).completable();

    }

    public Future<String> asyncMethodWithReturnType() {
        System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName());

        try {
            Thread.sleep(180000);
            nexacroService.findByAll().stream().forEach(m -> log.info("====================asyncTest="+m.toString()));
            return new AsyncResult<>("***********작업 완료*********** !!!!");
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public String isDone(Future<?> future) {
        String resultMessage = "";
        try {
            Thread.sleep(1000);
            while (true) {
                if (future.isDone()) {
                    log.info("Result from asynchronous process - " + future.get());
                    resultMessage = "Result from asynchronous process";
                    return resultMessage;
                }
                log.info("Continue doing something else. ");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return resultMessage;
    }



}
