package com.example.di.PassiveDi;

//@Configuration
public class DiConfig {

//    @Bean
    public CaffeService caffeService(){
        return new CaffeService(americano());
    }
//    @Bean
    public Americano americano(){
        return new Americano();
    }
//    @Bean
    public Juice juice(){
        return new Juice();
    }

}
