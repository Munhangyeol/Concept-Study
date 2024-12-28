package com.example.di.transactional;

import com.example.transactional.order.NotEnoughMoneyException;
import com.example.transactional.order.Order;
import com.example.transactional.order.OrderRepository;
import com.example.transactional.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

@SpringBootTest
@Slf4j
public class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void complete() throws NotEnoughMoneyException {

        //given
        Order order = new Order();
        order.setUsername("정상");

        //when
        orderService.order(order);
        //then
        Assertions.assertEquals("완료", orderRepository.findById(order.getId()).get().getPayStatus());

    }

    //runtime Exception의 경우 복구 불가능한 에러이자, 언체크예외로써, 시스템 에러로 인식해서, 해당
    //트랜젝션이 끝날 때 롤백을 하므로,repository에 저장이 안되어 있음
    @Test
    void runtimeException() {

        //given
        Order order = new Order();
        order.setUsername("예외");

        //when
        Assertions.assertThrows(RuntimeException.class, ()->orderService.order(order));

        //then
        Assertions.assertTrue(orderRepository.findById(order.getId()).isEmpty());
    }
    //비즈니스 예외의 경우 대기 상태로 주문을 저장해 두어야 하므로, 롤백이 되어서는 안되고 커밋을 해야함
    //체크 예외를 스프링은 비즈니스 예외로 간주하고, 일반적으로 커밋을 진행하고, 해당 비즈니스 예외에 맞는
    // 동작을 따로 수행하는 것이 일반적임
    @Test
    void bizException(){
        //given
        Order order = new Order();
        order.setUsername("잔고부족");

        //when
        try {
            orderService.order(order);
            log.info("잔고 부족 예외가 발생합니다.");
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내");
        }
        //then
        Assertions.assertEquals("대기",orderRepository.findById(order.getId()).get().getPayStatus());
    }

}
