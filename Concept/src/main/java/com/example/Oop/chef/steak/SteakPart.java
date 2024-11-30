package com.example.Oop.chef.steak;

public class SteakPart {
    protected String utensil;
    protected String butter;
    public void readyToCook(){
        System.out.println("Ready To Cook");
    }
    public void cook(){
        System.out.println(utensil+" 과 "+butter+" 으로 요리합니다");
    }
    public void resting(){
        System.out.println("Resting");
    }
}
