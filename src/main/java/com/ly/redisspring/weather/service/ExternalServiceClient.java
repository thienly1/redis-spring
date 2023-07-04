package com.ly.redisspring.weather.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExternalServiceClient {

    // this method will put a random number for any zip from 60 to 100
    @CachePut(value = "weather", key = "#zip")
    public int getWeatherInfo(int zip){
        return ThreadLocalRandom.current().nextInt(60, 100);
    }
}
