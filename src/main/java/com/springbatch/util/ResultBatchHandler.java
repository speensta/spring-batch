package com.springbatch.util;

import com.springbatch.domain.ItemDetail;
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
public class ResultBatchHandler implements ResultHandler<Object> {

    private List<Object> resultList = new ArrayList<>();

    @Override
    public void handleResult(ResultContext resultContext) {
        ItemDetail itemDetail = (ItemDetail) resultContext.getResultObject();
        resultList.add(itemDetail);
        log.debug("****************************itemDetail*********"+itemDetail);
    }
}
