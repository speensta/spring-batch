package com.springbatch.config.composite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompositeWriterSecond  implements ItemWriter {
    @Override
    public void write(List list) throws Exception {
        list.forEach(item -> {
            log.info("======================CompositeWriterSecond file write========================");
        });
    }
}
