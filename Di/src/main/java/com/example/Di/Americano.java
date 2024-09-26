package com.example.Di;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class Americano {
    private int price;
    public Americano(){
        price=4000;
    }

}
