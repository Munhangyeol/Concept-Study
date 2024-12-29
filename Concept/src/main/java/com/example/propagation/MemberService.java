package com.example.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Transactional
    public void joinV1(String username){
        log.info(" == memberRepository 호출 시작 == ");
        memberRepository.save(new Member(username));
        log.info(" == memberRepository 호출 종료 == ");
        log.info(" == logRepository 호출 시작 == ");
        logRepository.save(new Log(username));
        log.info(" == logRepository 호출 종료 == ");

    }
    @Transactional
    public void joinV2(String username){
        Log logMessage = new Log(username);
        log.info(" == memberRepository 호출 시작 == ");
        memberRepository.save(new Member(username));
        log.info(" == memberRepository 호출 종료 == ");
        log.info(" == logRepository 호출 시작 == ");
        try {
            logRepository.save(logMessage);
        }catch (RuntimeException e){
            log.info("log 저장에 실패하였습니다. logMessage={}",logMessage.getMessage());
        }
        log.info(" == logRepository 호출 종료 == ");

    }

}
