package com.springbatch.service.sse;

import com.springbatch.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupEvent {
    private Member member;
}

