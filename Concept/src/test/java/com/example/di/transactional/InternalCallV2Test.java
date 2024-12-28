package com.example.di.transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class InternalCallV2Test {
    @Autowired
    CallService callService;
    @Test
    void printProxy() {
        log.info("callService class={}", callService.getClass());
    }
    @Test
    void externalCallV2() {
        //internal이 수행되는 시점에서 callService는 프록시 객체가 아닌 실제 객체이다.
        // 그러나 internal이 수행되는 객체가 InternalService로 프록시 객체이므로,
        // 트랜젝션이 잘 적용됨
        callService.external();
    }


    @TestConfiguration
    static class InterncalCallV2Config{

        @Bean
        public CallService callService(){
            return new CallService(internalService());
        }
        @Bean
        public InternalService internalService(){
            return new InternalService();
        }
    }



    @RequiredArgsConstructor
    static class CallService{
        private final InternalService service;

        public void external(){
            log.info("call external");
            printTxInfo();
            service.internal();

        }
        public void printTxInfo(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active={}",isActive);
        }
    }
    static class InternalService{
        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }
        public void printTxInfo(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active={}",isActive);
        }

    }
}
