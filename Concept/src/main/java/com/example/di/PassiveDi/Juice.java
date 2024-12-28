package com.example.di.PassiveDi;

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
