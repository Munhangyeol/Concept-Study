package com.example.di.di;

import com.example.di.PassiveDi.CaffeService;
import com.example.di.PassiveDi.Juice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CaffeServiceTest {

    @Test
    @DisplayName("판 아메리카노의 가격은 4000원입니다")
    public void sellAmericano(){
        //given
       CaffeService caffeService;
        //when
       caffeService=new CaffeService(new Juice());
        //then
        Assertions.assertEquals(caffeService.sellJuice(),4000);
    }
}
