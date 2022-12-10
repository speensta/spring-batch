package com.springbatch.config.composite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
public class CompositeItemWriterDelegate<T> implements ItemStreamWriter<T>, InitializingBean {

    private List<ItemWriter<? extends T>> delegates;

    private boolean ignoreStream = false;


    public void setIgnoreStream(boolean ignoreStream) {
        this.ignoreStream = ignoreStream;
    }

    public void setDelegates(List<ItemWriter<? extends T>> delegates) {
        this.delegates = delegates;
    }


    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        for (ItemWriter<? extends T> writer : delegates) {
            if (!ignoreStream && (writer instanceof ItemStream)) {
                ((ItemStream) writer).open(executionContext);
            }
        }
    }

    @Override
    public void close() throws ItemStreamException {
        for (ItemWriter<? extends T> writer : delegates) {
            if (!ignoreStream && (writer instanceof ItemStream)) {
                ((ItemStream) writer).close();
            }
        }
    }

    @Override
    public void write(List item) throws Exception {
        for(ItemWriter<? extends T> itemWriter : this.delegates) {
            itemWriter.write(item);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(delegates,"=================== itemWriter is not be null ========");
        Assert.notEmpty(delegates,"=================== itemWriter is not be null ========");
    }
}
