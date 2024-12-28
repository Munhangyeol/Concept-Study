package com.example.di.PassiveDi;

import lombok.Setter;

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
