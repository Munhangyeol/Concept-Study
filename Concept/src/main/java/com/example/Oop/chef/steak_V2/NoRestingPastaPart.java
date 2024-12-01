package com.example.Oop.chef.steak_V2;

import com.example.Oop.chef.Utensil.Utensil;

import java.util.Arrays;

public class NoRestingPastaPart extends SteakPart implements CookSteak,ReadySteak{

    public NoRestingPastaPart(String steak_type){
        this.steak_type=steak_type;

    }
    @Override
    public void readyToCook(Utensil[] utensils,String butter){
        utensilsForSteak=utensils;
        this.butter=butter;
        System.out.println("Ready To Cook");
    }
    @Override
    public void cook(){
        Arrays.stream(utensilsForSteak).forEach(
                System.out::println);
        System.out.println("과" +butter+" 으로 요리합니다");
    }

}
