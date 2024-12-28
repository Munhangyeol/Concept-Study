package com.example.di.transactional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxApplyBasicTest {
    @Autowired BasicService service;
    @TestConfiguration
    static class TxApplyBasicConfig{
        @Bean
        public BasicService basicService(){
            return new BasicService();
        }
    }

    @Test
    void checkProxy(){
        log.info("Aop BasicService={} ",service.getClass());
        Assertions.assertTrue(AopUtils.isAopProxy(service));
    }

    @Test
    void txTest(){
        service.tx();
        service.nonTx();
    }


    static class BasicService{
        //선언형 트랜젝션이 하나라도붙어 있으면, 의존 관계 주입시 해당 클래스를 프록시로 감싸서 주입해줌.
        //이것이 가능한 이유는 프록시 객체가 해당 클래스의 자식 클래스 이기 때문에 가능한 것임.
        @Transactional
        public void tx(){
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active={}",txActive);
        }
        public void nonTx(){
            log.info("call nonTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active={}",txActive);

        }


    }
}
