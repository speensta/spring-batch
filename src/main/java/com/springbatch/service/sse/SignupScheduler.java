package com.springbatch.service.sse;

import com.springbatch.domain.Member;
import com.springbatch.service.NexacroService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class SignupScheduler {
    private final NexacroService nexacroService;

    @Scheduled(fixedDelay = 2000)
    public void signupTask() {
        nexacroService.create(this.createTestUser());
    }

    private Member createTestUser() {
        Random random = new Random();
        return Member.builder()
                .name("test"+random.nextInt())
                .memberid("user"+random.nextInt())
                .email("test"+random.nextInt()+"@naver.com")
                .password(random.nextInt()+"testtestest")
                .isdel("N")
                .build();
    }
}
