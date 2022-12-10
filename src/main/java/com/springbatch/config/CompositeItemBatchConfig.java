package com.springbatch.config;

import com.springbatch.config.composite.CompositeItemWriterDelegate;
import com.springbatch.config.composite.CompositeWriterFirst;
import com.springbatch.config.composite.CompositeWriterSecond;
import com.springbatch.domain.ItemHeader;
import com.springbatch.dto.ItemHeaderDTO;
import com.springbatch.service.ProcessService;
import com.springbatch.service.TestDBService;
import com.springbatch.util.JobListener;
import com.springbatch.util.JobNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompositeItemBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    private final static int CHUNKSIZE = 10;

    private static final int POOLSIZE = 2;

    private final TestDBService testDBService;

    private final ProcessService processService;

    private final CompositeWriterFirst compositeWriterFirst;
    private final CompositeWriterSecond compositeWriterSecond;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    @Bean(name = "batchExecutor")
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(POOLSIZE);
        executor.setMaxPoolSize(POOLSIZE);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }


    @Bean
    public Job compositeJob() {
        return jobBuilderFactory.get(JobNames.compositeJob.name())
                .incrementer(new RunIdIncrementer())
                .start(stepChunkComposite1(null))
                .listener(new JobListener())
                .build();

    }

    @Bean
    @JobScope
    public Step stepChunkComposite1(@Value("#{jobParameters['todate']}") String todate) {

        log.info("***************stepChunkComposite1*********** {} ", todate);

        return stepBuilderFactory.get("stepChunkComposite1")
                .chunk(CHUNKSIZE)
                .reader(compositeItemHeaderReader(null))
                .processor(compositeItemProcessor())
                .writer(itemWriterDelegate())
                .taskExecutor(this.executor())
                .throttleLimit(POOLSIZE)
                .build();
    }


    @Bean
    @JobScope
    public Step stepChunkComposite2(@Value("#{jobParameters['todate']}") String todate) {

        log.info("***************todate***********" );

        return stepBuilderFactory.get("stepChunkComposite2")
                .chunk(CHUNKSIZE)
                .reader(compositeItemDetailReader(null))
                .writer(compositeItemHeaderPriceWriter(null))
                .build();
    }


    @Bean
    @StepScope
    public MyBatisPagingItemReader<ItemHeader> compositeItemHeaderReader(@Value("#{jobParameters['todate']}") String todate) {

        log.info("***************compositeItemHeaderReader***********" );

        ItemHeaderDTO itemHeaderDTO = new ItemHeaderDTO();

        LocalDate localDate = LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate localDate2 = LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd"));

        itemHeaderDTO.setStartDt(localDate);
        itemHeaderDTO.setEndDt(localDate2);

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("startDt", "20221001");
        parameterMap.put("endDt", "20221031");
//        List<ItemHeader> itemHeaderList = testDBService.findHeaderByAll(parameterMap);


        MyBatisPagingItemReader myBatisPagingItemReader = new MyBatisPagingItemReader();
        myBatisPagingItemReader.setQueryId("");
        myBatisPagingItemReader.setParameterValues(null);


        return new MyBatisPagingItemReaderBuilder<ItemHeader>()
                .pageSize(CHUNKSIZE)
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.springbatch.service.impl.mapper.TestDBMapper.findHeaderByAll")
                .parameterValues(parameterMap)
                .build();
    }


    @Bean
    @StepScope
    public MyBatisPagingItemReader<ItemHeader> compositeItemDetailReader(@Value("#{jobParameters['todate']}") String todate) {

        ItemHeaderDTO itemHeaderDTO = new ItemHeaderDTO();

        LocalDate localDate = LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate localDate2 = LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd"));

        itemHeaderDTO.setStartDt(localDate);
        itemHeaderDTO.setEndDt(localDate2);

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("startDt", "20221001");
        parameterMap.put("endDt", "20221031");

        log.info("========================== chunk batch reader ===========================");

        return new MyBatisPagingItemReaderBuilder<ItemHeader>()
                .pageSize(CHUNKSIZE)
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.springbatch.service.impl.mapper.TestDBMapper.findHeaderByAll")
                .parameterValues(parameterMap)
                .build();
    }


    @Bean
    public ItemProcessor<? super Object,?> compositeItemProcessor() {

//        sqlSessionFactory.openSession().flushStatements();
        log.info("***************compositeItemProcessor***********" );
        return items -> {

            return items;
        };

    }


//    @Bean
//    public ItemWriter<? super Object> itemWriterFirst() {
//        return items -> {
//            log.info("============================== itemWriterFirst =================================");
//        };
//    }
//
//    @Bean
//    public ItemWriter<? super Object> itemWriterSecond() {
//        return items -> {
//            log.info("============================== itemWriterSecond =================================");
//        };
//    }

    @Bean
    public CompositeItemWriterDelegate<? super Object> itemWriterDelegate() {
        log.info("=================itemWriterDelegate==================");
        CompositeItemWriterDelegate compositeItemWriter = new CompositeItemWriterDelegate();
        compositeItemWriter.setDelegates(Arrays.asList(compositeWriterFirst, compositeWriterSecond));
        return compositeItemWriter;
    }

    @Bean
    @StepScope
    public ItemWriter<? super Object> compositeItemHeaderPriceWriter(@Value("#{jobParameters['todate']}") String todate) {
        return items -> {

            for(Object header : items) {
                log.info("=================compositeItemHeaderPriceWriter==================");
                ItemHeader itemHeader = (ItemHeader) header;
                sqlSessionFactory.openSession().update("com.springbatch.service.impl.mapper.TestDBMapper.updateHeader", header);
//                sqlSessionFactory.openSession().update("com.springbatch.service.impl.mapper.TestDBMapper.updateDetail", itemDetail);
            }

        };
    }


    @Bean
    @StepScope
    public ItemWriter<? super Object> compositeItemDetailPriceWriter(@Value("#{jobParameters['todate']}") String todate) {
        return items -> {
            final AtomicInteger index = new AtomicInteger();
            log.info("=============start");
            items.forEach(item -> {
//                final int idx = index.getAndIncrement();
//                log.info("============="+idx);
                log.info("Chunk Item Detail Writer============="+item.toString());
            });
            log.info("=============end");
        };
    }


}
