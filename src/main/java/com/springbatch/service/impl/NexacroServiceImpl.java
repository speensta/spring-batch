package com.springbatch.service.impl;

import com.springbatch.domain.Member;
import com.springbatch.service.NexacroService;
import com.springbatch.service.impl.mapper.NexacroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NexacroServiceImpl implements NexacroService {

    private final NexacroMapper nexacroMapper;

    public List<Member> findByAll() {
        return nexacroMapper.findByAll();
    }
}
