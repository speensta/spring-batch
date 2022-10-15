package com.springbatch.config;

import com.springbatch.domain.Item;
import com.springbatch.domain.ItemDetail;
import com.springbatch.domain.ItemHeader;
import com.springbatch.dto.ItemHeaderDTO;
import com.springbatch.service.TestDBService;
import com.springbatch.util.JobListener;
import com.springbatch.util.JobNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchChunkConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    private final static int chunkSize = 500;

    private final TestDBService testDBService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get(JobNames.chunkJob.name())
                .incrementer(new RunIdIncrementer())
                .listener(new JobListener())
                .start(stepChunk1(null))
                .next(stepChunk2(null))
                .build();

    }

    @Bean
    @JobScope
    public Step stepChunk1(@Value("#{jobParameters['todate']}") String todate) {

        log.info("***************todate***********" + todate );

        return stepBuilderFactory.get("stepChunk1")
                .chunk(chunkSize)
                .reader(itemHeaderReader(null))
                .writer(itemHeaderPriceWriter(null))
                .build();
    }


    @Bean
    @JobScope
    public Step stepChunk2(@Value("#{jobParameters['todate']}") String todate) {

        log.info("***************todate***********" );

        return stepBuilderFactory.get("stepChunk2")
                .chunk(chunkSize)
                .reader(itemDetailReader(null))
                .writer(itemDetailPriceWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public MyBatisPagingItemReader<ItemHeader> itemHeaderReader(@Value("#{jobParameters['todate']}") String todate) {

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
                .pageSize(chunkSize)
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.springbatch.service.impl.mapper.TestDBMapper.findHeaderByAll")
                .parameterValues(parameterMap)
                .build();
    }


    @Bean
    @StepScope
    public MyBatisPagingItemReader<ItemHeader> itemDetailReader(@Value("#{jobParameters['todate']}") String todate) {

        ItemHeaderDTO itemHeaderDTO = new ItemHeaderDTO();

        LocalDate localDate = LocalDate.parse("20221001", DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate localDate2 = LocalDate.parse("20221031", DateTimeFormatter.ofPattern("yyyyMMdd"));

        itemHeaderDTO.setStartDt(localDate);
        itemHeaderDTO.setEndDt(localDate2);

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("startDt", "20221001");
        parameterMap.put("endDt", "20221031");

        return new MyBatisPagingItemReaderBuilder<ItemHeader>()
                .pageSize(chunkSize)
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.springbatch.service.impl.mapper.TestDBMapper.findHeaderByAll")
                .parameterValues(parameterMap)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<? super Object> itemHeaderPriceWriter(@Value("#{jobParameters['todate']}") String todate) {
        return items -> {
            log.info("=============start");
            Map parameterMap1 = new HashMap<String, Object>();

            
            for(Object header : items) {
                ItemHeader itemHeader = (ItemHeader) header;

                parameterMap1.put("itemid", itemHeader.getSeq());

//                MyBatisPagingItemReader<Item> myBatisPagingItemReader =
//                                            new MyBatisPagingItemReaderBuilder<Item>()
//                            .pageSize(chunkSize)
//                            .sqlSessionFactory(sqlSessionFactory)
//                            .queryId("com.springbatch.service.impl.mapper.TestDBMapper.findByItem")
//                            .parameterValues(parameterMap1)
//                            .build();

//                Item item = myBatisPagingItemReader.read();

                Item item = sqlSessionFactory.openSession()
                        .selectOne("com.springbatch.service.impl.mapper.TestDBMapper.findByItem", parameterMap1);
//                Item item = testDBService.findByItem(parameterMap1);

                log.info("Chunk Item Header Writer============="+item.getPrice());

            }
            

//            items.forEach(item -> {
//
//                ItemHeader itemHeader = (ItemHeader) item;
//
//                parameterMap1.put("itemid", itemHeader.getSeq());
//
//                test[0] = new MyBatisPagingItemReaderBuilder<ItemHeader>().pageSize(chunkSize)
//                        .sqlSessionFactory(sqlSessionFactory)
//                        .queryId("com.springbatch.service.impl.mapper.TestDBMapper.findByItem")
//                        .parameterValues(parameterMap1).build();
//
//                try {
//                    Item itemDomain = test[0].read();
//                    log.info("Chunk Item Header Writer============="+itemDomain.getPrice());
//
//
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
            log.info("=============end");
        };
    }


    @Bean
    @StepScope
    public ItemWriter<? super Object> itemDetailPriceWriter(@Value("#{jobParameters['todate']}") String todate) {
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

//    @Bean
//    @StepScope
//    public PagingQueryProvider createQueryProvider() {
//
//        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
//        queryProviderFactoryBean.setDataSource(dataSource);
//        queryProviderFactoryBean.setSelectClause("memberid, name, email");
//        queryProviderFactoryBean.setFromClause("from member");
//        queryProviderFactoryBean.setWhereClause("where memberid like '%test%'");
//        Map<String, Order> sortKey = new HashMap<>();
//
//        sortKey.put("memberid", Order.ASCENDING);
//
//        queryProviderFactoryBean.setSortKeys(sortKey);
//
//        try {
//            return queryProviderFactoryBean.getObject();
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//
//        return null;
//    }

}
