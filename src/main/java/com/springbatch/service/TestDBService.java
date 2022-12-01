package com.springbatch.service;

import com.springbatch.domain.Item;
import com.springbatch.domain.ItemDetail;
import com.springbatch.domain.ItemHeader;
import com.springbatch.dto.ItemHeaderDTO;

import java.util.List;
import java.util.Map;

public interface TestDBService {
    List<Map<String, Object>> mybatisTest();

    List<ItemHeader> findHeaderByAll(Map<String ,Object> map);

    void createHeader(ItemHeader itemHeader);

    void updateHeader(ItemHeader itemHeader);

    void deleteHeader(ItemHeader itemHeader);


    List<ItemDetail> findDetailByAll(Map<String ,Object> map);

    List<ItemDetail> findDetailBySeq(Map<String ,Object> map);

    void createDetail(ItemDetail itemDetail);

    void updateDetail(ItemDetail itemDetail);

    void deleteDetail(ItemDetail itemDetail);

    Item findByItem(Map<String ,Object> map);
}
