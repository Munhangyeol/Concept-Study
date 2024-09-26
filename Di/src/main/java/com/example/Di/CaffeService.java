package com.example.Di;


public class CaffeService {
    private Drink drink;
    public CaffeService(Drink drink){
        this.drink=drink;
    }
    public int sellAmericano(){
        System.out.println("Americano price: "+drink.getPrice());
        return drink.getPrice();
    }

}
