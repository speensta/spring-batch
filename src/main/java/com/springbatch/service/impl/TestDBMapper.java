package com.springbatch.service.impl;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestDBMapper {

    List<Map<String,Object>> mybatisTest();

}
