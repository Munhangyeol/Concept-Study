package com.example.Oop.chef.steak;

public class SirloinSteakPart extends SteakPart {
    public SirloinSteakPart(){

    }
    @Override
    public void cook(){
        System.out.println(utensil+" 과 "+butter+" 으로 Sirloin으로 요리합니다");
    }
}
