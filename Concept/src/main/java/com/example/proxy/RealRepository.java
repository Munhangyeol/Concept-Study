package com.example.proxy;


import org.springframework.stereotype.Repository;


@Repository
//@Transactional
public class RealRepository extends DBRepository{
    protected RealRepository(Dao dao) {
        super(dao);
    }
}
