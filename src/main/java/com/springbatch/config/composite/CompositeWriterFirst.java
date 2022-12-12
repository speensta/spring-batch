package com.springbatch.config.composite;

import com.springbatch.domain.ItemHeader;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompositeWriterFirst implements ItemWriter {

    private final SqlSessionFactory sqlSessionFactory;

    private HashMap<String, Object> paramMap;

    @Override
    public void write(List list) throws Exception {
        list.forEach(item -> {
            try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
                ItemHeader header = (ItemHeader) item;
                header.setPrice(0);
                header.setTotalprice(0);
                header.setRate(0);
                paramMap = new HashMap<>();
                paramMap.put("price", 10000);
                paramMap.put("totalprice", 100000);
                paramMap.put("rate", 1);
                paramMap.put("seq", header.getSeq());

                sqlSession.update("com.springbatch.service.impl.mapper.TestDBMapper.updateHeader", header);
                sqlSession.flushStatements();
                sqlSession.clearCache();
            }
            log.info("======================CompositeWriterFirst list update========================{}", item.toString());
        });
//        log.info("======================CompositeWriterFirst list updateHeaderbulk========================");
//        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
//            sqlSession.update("com.springbatch.service.impl.mapper.TestDBMapper.updateHeaderbulk", list);
//            sqlSession.flushStatements();
//            sqlSession.clearCache();
//        }
    }
}
