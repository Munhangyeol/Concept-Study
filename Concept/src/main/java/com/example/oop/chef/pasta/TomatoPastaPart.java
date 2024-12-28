package com.example.oop.chef.pasta;

public class TomatoPastaPart extends PastaPart{
    private int salt;
    private int recipe;
    public int cook(){
        System.out.println("토마토 파스타가 요리되었습니다!");
        return salt*recipe;
    }
}
