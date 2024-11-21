package com.example.Di.PassiveDi;

import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
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
