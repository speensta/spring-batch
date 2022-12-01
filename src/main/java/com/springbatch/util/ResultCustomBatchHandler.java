package com.springbatch.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Getter @Setter
public class ResultCustomBatchHandler<T> implements ResultHandler<T> {

    private List<T> resultList = new ArrayList<>();

    @Override
    public void handleResult(ResultContext<? extends T> resultContext) {
        T obj = resultContext.getResultObject();
        resultList.add(obj);
    }
}
