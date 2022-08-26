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
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsyncService {

    private final NexacroService nexacroService;

    @Async
    public void asyncTest() throws InterruptedException {

        Thread.sleep(10000);

        nexacroService.findByAll().stream().forEach(m -> log.info("====================asyncTest="+m.toString()));


    }

    @Async("asyncExecutor")
    public Future<String> asyncMethodWithReturnType() {
        System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName());

        try {
            Thread.sleep(300000);
            nexacroService.findByAll().stream().forEach(m -> log.info("====================asyncTest="+m.toString()));
            return new AsyncResult<>("***********작업 완료*********** !!!!");
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        return null;
    }

}
