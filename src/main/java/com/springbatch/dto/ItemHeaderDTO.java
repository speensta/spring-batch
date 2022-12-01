package com.springbatch.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ItemHeaderDTO {
    private int seq;
    private String itemcd ;
    private LocalDate startDt;
    private LocalDate endDt;
}
