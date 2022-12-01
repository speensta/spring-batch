package com.springbatch.service.impl;

import com.springbatch.domain.Item;
import com.springbatch.domain.ItemDetail;
import com.springbatch.domain.ItemHeader;
import com.springbatch.service.ProcessService;
import com.springbatch.service.impl.mapper.TestDBMapper;
import com.springbatch.util.ResultBatchHandler;
import com.springbatch.util.ResultCustomBatchHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ResultHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final TestDBMapper testDBMapper;

    private final SqlSessionTemplate sqlSession;


    public List<Object> selectItemDetailList() {

        ResultCustomBatchHandler resultCustomBatchHandler = new ResultCustomBatchHandler<>();

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("startDt", "20220101");
        parameterMap.put("endDt", "20221231");

        testDBMapper.findDetailByAllList(parameterMap, resultCustomBatchHandler);

        return resultCustomBatchHandler.getResultList();
    }


    @Transactional(value = "transactionManager2", propagation = Propagation.NOT_SUPPORTED)
    public void process1Batch(ItemHeader itemHeader) {

        Map<String, Object> map = new HashMap();

        map.put("itemid", itemHeader.getSeq());
        map.put("seq", itemHeader.getSeq());
        Item item = testDBMapper.findByItem(map);

        itemHeader.setPrice(Math.round(item.getPrice() * itemHeader.getRate()));

        testDBMapper.updateHeader(itemHeader);
    }




}
