package com.example.Di;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Setter
public class Americano implements Drink{
    private int price;
    public Americano(){
        price=4000;
    }

    @Override
    public int getPrice() {
        return price;
    }
}
