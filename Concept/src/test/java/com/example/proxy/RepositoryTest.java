package com.example.proxy;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
public class RepositoryTest {

    @Autowired
    private DBRepository repository;

    @Test
    void isReturn1(){
        log.info(repository.getClass().getName());
        Assertions.assertEquals(1, repository.add());
        Assertions.assertEquals(1, repository.add2());
        log.info("add1={},add2={}",repository.add(),repository.add2());
    }

}
