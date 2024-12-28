package com.example.di.transactional;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class InitTxTest {

    @Autowired
    Hello hello;

    @Test
    void go(){

    }



    @TestConfiguration
    static class InitTxConfig{

        @Bean
        public Hello hello(){
            return new Hello();
        }
    }



    static class Hello{

        //PostConstruct가 적용된 후에 AOP가 적용되므로 트랜젝션이 적용이 안된다.
        //따라서 완전히 aop까지 적용된 후에 트랜젝션을 수행해야함
        @PostConstruct
        @Transactional
        public void initV1(){
            log.info("initV1");
            printTxInfo();
        }
        // 아래의 이벤트는 트랙젝션 aop를 포함한 스프링 컨테이너가 완전히 생성되고 난 다음에 이벤트가 붙은 메서드를 호출해둠
        // 따라서 트랜젝션이 잘 적용.
        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            log.info("initV2");
            printTxInfo();
        }


        public void printTxInfo(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active={}",isActive);
        }
    }
}
