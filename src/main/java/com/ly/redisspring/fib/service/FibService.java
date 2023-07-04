package com.ly.redisspring.fib.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FibService {

    @Cacheable(value = "math:fib", key = "#index")
    public int getFib(int index, String name){
        System.out.println("calculating fib for " + index + ", name: " + name); // this will print out every time you call for url. But if we use key = "#index", it will print out every you call url with new index
        return this.fib(index);
    }

    private int fib(int index){
        if(index < 2)
        return index;
        return  fib(index-1);
    }
}
