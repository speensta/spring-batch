package com.springbatch.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Builder
@Alias("itemHeader")
public class ItemHeader {
    private int seq;
    private double price;
    private double rate;
    private int totalprice;
    private String itemcd ;
    private String createuser;
    private String updateuser;
}
