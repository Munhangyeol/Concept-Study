package com.example.Oop.chef.steak_V2;

import com.example.Oop.chef.Utensil.Utensil;

import java.util.Arrays;

public class SirloinSteakPart extends SteakPart implements CookSteak,ReadySteak,RestSteak{
    public SirloinSteakPart(String steak_type){
        this.steak_type=steak_type;
    }
    @Override
    public void readyToCook(Utensil[] utensils, String butter){
        utensilsForSteak=utensils;
        this.butter=butter;
        System.out.println("Ready To Cook");
    }
    public void cook(){
        Arrays.stream(utensilsForSteak).forEach(
                System.out::println);
        System.out.println("과" +butter+" 으로 요리합니다");
    }
    public void resting(){
        System.out.println("Resting");
    }





}
