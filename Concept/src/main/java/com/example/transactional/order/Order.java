package com.example.transactional.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Slf4j
@Setter
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private String username; //정상, 예외, 잔고 부족
    private String payStatus;// 대기,완료

}
