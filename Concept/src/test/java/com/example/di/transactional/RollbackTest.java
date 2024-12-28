package com.example.di.transactional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService service;
    @Test
    void runtimeException() {
        assertThatThrownBy(() -> service.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }
    //checkedException의 경우 비즈니스 예외로 처리되는 경우가 일반적이라서 롤백을 하지 않고
    // 커밋을 하는 것이 일반적인 상황임
    @Test
    void checkedException() {
        assertThatThrownBy(() -> service.checkedException())
                .isInstanceOf(MyException.class);
    }
    //따라서 checkedException을 할 때 롤백을 하고 싶다면 rollbackFor를 이용하면 됨.
    @Test
    void rollbackFor() {
        assertThatThrownBy(() -> service.rollbackFor())
                .isInstanceOf(MyException.class);
    }




    @TestConfiguration
    static class RollbackTestConfig {
        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }


    static class RollbackService{

        @Transactional
        public void runtimeException(){
            log.info("call runtimeException");
            throw new  RuntimeException();
        }

        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }




    }
    static class MyException extends Exception{

    }

}
