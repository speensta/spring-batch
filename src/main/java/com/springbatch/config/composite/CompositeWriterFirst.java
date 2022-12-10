package com.springbatch.config.composite;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompositeWriterFirst implements ItemWriter {

    private final SqlSessionFactory sqlSessionFactory;

    @Override
    public void write(List list) throws Exception {
        list.forEach(item -> {
            try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
                sqlSession.update("com.springbatch.service.impl.mapper.TestDBMapper.updateHeader", item);
            }
            log.info("======================CompositeWriterFirst list update========================{}", item.toString());
        });
    }
}
