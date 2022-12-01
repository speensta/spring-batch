package com.springbatch.service.impl;

import com.springbatch.domain.Item;
import com.springbatch.domain.ItemDetail;
import com.springbatch.domain.ItemHeader;
import com.springbatch.dto.ItemHeaderDTO;
import com.springbatch.service.TestDBService;
import com.springbatch.service.impl.mapper.TestDBMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestDBServiceImpl implements TestDBService {
    private final TestDBMapper testDBMapper;

    @Override
    public List<Map<String, Object>> mybatisTest() {
        return testDBMapper.mybatisTest();
    }

    @Override
    public List<ItemHeader> findHeaderByAll(Map<String ,Object> map) {
        return testDBMapper.findHeaderByAll(map);
    }

    @Override
    public void createHeader(ItemHeader itemHeader) {
        testDBMapper.createHeader(itemHeader);
    }

    @Override
    public void updateHeader(ItemHeader itemHeader) {
        testDBMapper.updateHeader(itemHeader);
    }

    @Override
    public void deleteHeader(ItemHeader itemHeader) {
        testDBMapper.deleteHeader(itemHeader);
    }



    @Override
    public List<ItemDetail> findDetailByAll(Map<String ,Object> map) {
        return testDBMapper.findDetailByAll(map);
    }

    @Override
    public List<ItemDetail> findDetailBySeq(Map<String ,Object> map) {
        return testDBMapper.findDetailByAll(map);
    }

    @Override
    public void createDetail(ItemDetail itemDetail) {
        testDBMapper.createDetail(itemDetail);
    }

    @Override
    public void updateDetail(ItemDetail itemDetail) {
        testDBMapper.updateDetail(itemDetail);
    }

    @Override
    public void deleteDetail(ItemDetail itemDetail) {
        testDBMapper.deleteDetail(itemDetail);
    }

    @Override
    public Item findByItem(Map<String ,Object> map) {
        return testDBMapper.findByItem(map);
    }
}
