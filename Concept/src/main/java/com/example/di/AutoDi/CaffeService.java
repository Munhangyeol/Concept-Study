package com.example.di.AutoDi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaffeService {
    private final Drink americano;

    @Autowired
    public CaffeService(Drink americano){
        this.americano=americano;
    }
    public int sellJuice(){
        System.out.println("drink price: "+americano.getPrice());
        return americano.getPrice();
    }

}
