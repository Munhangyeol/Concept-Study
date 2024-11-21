package com.example.Di.PassiveDi;

import org.springframework.stereotype.Component;

//@Component
public class Juice implements Drink{
    int price;
    public Juice(){
        this.price=5000;
    }
    @Override
    public int getPrice() {
        return price;
    }
}
