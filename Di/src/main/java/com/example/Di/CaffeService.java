package com.example.Di;


public class CaffeService {
    private Americano americano;
    public CaffeService(Americano americano){
        this.americano=americano;
    }
    public int sellAmericano(){
        System.out.println("Americano price: "+americano.getPrice());
        return americano.getPrice();
    }

}
