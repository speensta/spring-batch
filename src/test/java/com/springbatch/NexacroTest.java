package com.springbatch;


import com.springbatch.domain.ItemDetail;
import com.springbatch.domain.ItemHeader;
import com.springbatch.domain.Member;
import com.springbatch.service.NexacroService;
import com.springbatch.service.TestDBService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class NexacroTest {

//    @Autowired
//    private NexacroService nexacroService;

    @Autowired
    private TestDBService testDBService;

    @Test
    public void init() {

//        for (int i=3;i<1000000;i++) {
//            nexacroService.create(Member.builder().memberid("test"+i)
//                    .email("tset"+i+"@naver.com")
//                    .name("test"+i)
//                    .url("http://test"+i+".co.kr")
//                    .isdel("N")
//                    .build());
//        }

        for (int i=1;i<100000;i++) {
            testDBService.createHeader(ItemHeader.builder().itemcd("000"+i).rate(11.3).createuser("test"+i).build());
            testDBService.createDetail(ItemDetail.builder().itemgbn('D').createuser("test"+i).build());
        }


    }

    @Test
    public void test1() {

//        List<Member> list = nexacroService.findByAll();
//
//        List<Member> test = list.stream().filter(m -> m.getMemberid().contains("1")).collect(Collectors.toList());
//
//        test.forEach(m -> {
//            m.setIsdel("Y");
//            nexacroService.update(m);
//        });
    }
}
