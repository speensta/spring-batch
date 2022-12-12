package com.springbatch.config.composite;

import com.springbatch.domain.ItemHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompositeWriterSecond<T> implements ItemWriter {
    @Override
    public void write(List list) throws Exception {
        list.forEach(item -> {
            log.info("======================CompositeWriterSecond file write========================{}", item.toString());
        });

        BeanWrapperFieldExtractor<T> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"seq", "price", "totprice", "rate"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<T> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        new FlatFileItemWriterBuilder<>()
                .name("itemWriter")
                .append(true)
                .encoding("UTF-8")
                .resource(new FileSystemResource("C:\\exportSample\\exportBatch.txt"))
                .lineAggregator((LineAggregator<Object>) lineAggregator)
                .build();
    }
}
