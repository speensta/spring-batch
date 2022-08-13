package com.springbatch.service.impl;

import com.springbatch.service.TestDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
