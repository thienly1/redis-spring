package com.ly.redisspring.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FibService {

    //have a stratery for cache evict
    @Cacheable(value = "math:fib", key = "#index")
    public int getFib(int index){
        System.out.println("calculating fib for " + index); // this will print out every time you call for url. But if we use key = "#index", it will print out every you call url with new index
        return this.fib(index);
    }

    @CacheEvict(value = "math:fib", key = "#index") //it means whenever this math:fib is invoked, go to this hash"math:fib", with the key = "#index" to clear this
    public void clearCache(int index){
        System.out.println("Clearing hash key");
    }

    //clear all entries with "math:fib every 10 seconds
    @Scheduled(fixedRate = 10_000) // 10_000 milliseconds
    @CacheEvict(value = "math:fib", allEntries = true)
    public void clearCache(){
        System.out.println("clearing all fib keys");
    }

    //intentional 2^N
    private int fib(int index){
        if(index < 2)
        return index;
        return  fib(index-1)+ fib(index-2);
    }
}
