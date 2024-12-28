package com.example.transactional.order;

public class NotEnoughMoneyException extends Exception{
    public NotEnoughMoneyException(String message){
        super(message);
    }
}
