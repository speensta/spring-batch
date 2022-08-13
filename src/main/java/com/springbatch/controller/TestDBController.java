package com.springbatch.controller;

import com.springbatch.service.impl.TestDBServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestDBController {

    private final TestDBServiceImpl testDBService;

    @GetMapping(value = "/testdb")
    public List<Map<String, Object>> testdb() {
        List<Map<String, Object>> dataList = null;
        dataList = testDBService.mybatisTest();
        return dataList;
    }

}
