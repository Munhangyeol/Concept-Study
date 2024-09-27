package com.example.Di;


public class CaffeService {
    private Drink drink;
    public CaffeService(Drink drink){
        this.drink=drink;
    }
    public int sellJuice(){
        System.out.println("drink price: "+drink.getPrice());
        return drink.getPrice();
    }

}
