package com.example.di.AutoDi;

import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Component
public class Americano implements Drink {
    private int price;
    public Americano(){
        price=4000;
    }

    @Override
    public int getPrice() {
        return price;
    }
}
