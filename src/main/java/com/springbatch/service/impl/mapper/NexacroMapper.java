package com.springbatch.service.impl.mapper;

import com.springbatch.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NexacroMapper {

    List<Member> findByAll();

    void create(Member member);

    void update(Member member);

    void delete(Member member);
}



