package com.springbatch.service;


import com.springbatch.domain.Member;
import com.springbatch.service.impl.mapper.NexacroMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

public interface NexacroService {
    List<Member> findByAll() ;

    void create(Member member);

    void update(Member member);

    void delete(Member member);
}
