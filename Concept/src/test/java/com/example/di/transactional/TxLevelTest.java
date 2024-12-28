package com.example.di.transactional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class TxLevelTest {
    @Autowired
    LevelService service;

    @Test
    void orderTest(){
        service.read();
        service.write();
    }


    @TestConfiguration
    static class TxApplyLevelConfig {
        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }



//    @Service
    // Transactional 어노테이션은 구체적인 것에 우선으로 적용되며, 클래스에 붙으면 public형인 모든 메서드에 적용됨
    @Transactional(readOnly = true)
    static class LevelService{

        @Transactional(readOnly = false)
        public void write(){
            log.info("call write");
            pringTxInfo();
        }
        public void read(){
            log.info("call read");
            pringTxInfo();
        }

        public void pringTxInfo(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx active={}",isActive);
            log.info("tx readOnly={}",readOnly);
        }

    }
}
