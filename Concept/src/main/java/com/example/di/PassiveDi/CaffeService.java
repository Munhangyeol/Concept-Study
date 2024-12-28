package com.example.di.PassiveDi;


//@Service
public class CaffeService {
    private final Drink drink;
    //@Autowired
    public CaffeService(Drink drink){
        this.drink=drink;
        this.sellJuice();
    }
    public int sellJuice(){
        System.out.println("drink price: "+drink.getPrice());
        return drink.getPrice();
    }

}
