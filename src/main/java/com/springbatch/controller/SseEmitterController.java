package com.springbatch.controller;

import com.springbatch.domain.Member;
import com.springbatch.service.AsyncService;
import com.springbatch.service.sse.SignupEvent;
import com.springbatch.service.sse.SignupScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SseEmitterController {


    @Autowired
    private SignupScheduler signupScheduler;

    private static final long SSE_SESSION_TIMEOUT = 30 * 1000L;

    private Set<SseEmitter> emitterSet = new CopyOnWriteArraySet<>();

    private final AsyncService asyncService;


    @GetMapping(value = "/member/notice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter signup(HttpServletRequest request) {
        log.info("SSE stream 접근 : {}", request.getRemoteAddr());

        SseEmitter emitter = new SseEmitter(SSE_SESSION_TIMEOUT);
        emitterSet.add(emitter);

        emitter.onTimeout(() -> emitterSet.remove(emitter));
        emitter.onCompletion(() -> emitterSet.remove(emitter));

        this.signupScheduler.signupTask();

        return emitter;
    }

    @Async
    @EventListener
    public void onSignupEvent(final SignupEvent signupEvent) {
        log.info("신규 회원 = {}, 이벤트 구독자 수 = {}", signupEvent.getMember(), emitterSet.size());

        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitterSet.forEach(emitter -> {
            try {
                emitter.send(signupEvent, MediaType.APPLICATION_JSON);
            } catch (Exception ignore) {
                deadEmitters.add(emitter);
            }
        });
        emitterSet.removeAll(deadEmitters);
    }

    @GetMapping(value = "/member/create")
    public String signupMember(HttpServletRequest request) {

        this.signupScheduler.signupTask();

        return "member";
    }

}
