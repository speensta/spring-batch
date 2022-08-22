package com.springbatch.domain;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Setter @Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Alias("member")
public class Member {
    private String memberid;
    private String email;
    private String isdel;
    private Timestamp moddate;
    private String name;
    private String password;
    private Timestamp regdate;
    private String url;
}
