package com.springbatch.config;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchChunkConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public final DataSource dataSource;

    @Bean
    public Job jobChunk() {
        return jobBuilderFactory.get("jobChunk")
                .incrementer(new RunIdIncrementer())
                .start(stepChunk1())
                .build();

    }

    @Bean
    public Step stepChunk1() {
        return stepBuilderFactory.get("stepChunk1")
                .chunk(10)
                .reader(memberItemReader())
                .writer(memberItemWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends Member> memberItemReader() {
        return new JdbcPagingItemReaderBuilder<Member>()
                .name("jdbcPagingReader")
                .pageSize(2)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Member.class))
                .queryProvider(createQueryProvider())
                .build();
    }

    private ItemWriter<? super Object> memberItemWriter() {
        return items -> {
            final AtomicInteger index = new AtomicInteger();
            log.info("=============start");
            items.forEach(item -> {
//                final int idx = index.getAndIncrement();
//                log.info("============="+idx);
                log.info("Chunk Item Writer============="+item.toString());
            });
            log.info("=============end");
        };
    }

    @Bean
    public PagingQueryProvider createQueryProvider() {

        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(dataSource);
        queryProviderFactoryBean.setSelectClause("memberid, name, email");
        queryProviderFactoryBean.setFromClause("from member");
        queryProviderFactoryBean.setWhereClause("where memberid like '%test%'");
        Map<String, Order> sortKey = new HashMap<>();

        sortKey.put("memberid", Order.ASCENDING);

        queryProviderFactoryBean.setSortKeys(sortKey);

        try {
            return queryProviderFactoryBean.getObject();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

}
