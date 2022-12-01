package com.springbatch.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Builder
@Alias("itemDetail")
public class ItemDetail {
    private int seq;
    private char itemgbn;
    private String itemcd ;
    private double price;
    private String vndcd;
    private String createuser;
    private String updateuser;
}
