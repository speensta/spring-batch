package com.springbatch.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Data
@Builder
@Alias("item")
public class Item {
    private int itemid;
    private String regname;
    private int price;
    private Timestamp moddate;
    private Timestamp regdate;
    private char isdel;
}
