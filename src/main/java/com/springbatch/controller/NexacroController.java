package com.springbatch.controller;

import com.nexacro.uiadapter17.spring.core.annotation.ParamDataSet;
import com.nexacro.uiadapter17.spring.core.data.NexacroResult;
import com.springbatch.domain.Member;
import com.springbatch.service.NexacroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NexacroController {

    private final NexacroService nexacroService;


    @GetMapping("/nexacro/list")
    public List<Member> findByAll() {
        List<Member> result = nexacroService.findByAll();
        return result;
    }

    @GetMapping("/list")
    public NexacroResult findByAll2() {
        List<Member> resultList = nexacroService.findByAll();
        NexacroResult nexacroResult = new NexacroResult();
        nexacroResult.addDataSet("ds_list", resultList);

        return nexacroResult;
    }




}
