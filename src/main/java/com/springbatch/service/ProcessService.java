package com.springbatch.service;

import com.springbatch.domain.ItemDetail;
import com.springbatch.domain.ItemHeader;

import java.util.List;

public interface ProcessService {

    List<Object> selectItemDetailList();

    void process1Batch(ItemHeader itemHeader);

}
