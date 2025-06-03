package com.example.proxy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;




@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class DBRepository {

    private final Dao dao;

    public int add(){
        return dao.add();
    }
    public final int add2(){
        return dao.add();
    }



}
