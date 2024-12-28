package com.example.di.transactional;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Call;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class InternalCallV1Test {
    @Autowired
    CallService callService;
    @Test
    void printProxy() {
        log.info("callService class={}", callService.getClass());
    }
    @Test
    void internalCall() {
        //callSevice가 프록시 객체이므로 트랜젝션이 적용됨
        callService.internal();
    }
    @Test
    void externalCall() {
        //internal이 수행되는 시점에서 callService는 프록시 객체가 아닌 실제 객체이다.
        // 따라서 이 때는 트랜젝션이 적용되지않는다.
        callService.external();
    }



    @TestConfiguration
    static class InterncalCallV1Config{

        @Bean
        public CallService callService(){
            return new CallService();
        }
    }



    static class CallService{

        public void external(){
            log.info("call external");
            printTxInfo();
            internal();

        }

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
