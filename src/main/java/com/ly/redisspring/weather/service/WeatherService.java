package com.ly.redisspring.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class WeatherService {

    @Autowired
    private ExternalServiceClient client;

    //this method return 0, but we have never seen 0 in output. because with redis. the client request for service. with @Cacheable,
    // this service will contact with the redis first. Redis will get information from @Scheduled service (@Scheduled service got external service updated)
    // then Redis will send back the service what requested by client the result of weather inf. then return info of weather at that time.
    @Cacheable("weather")
    public int getInfo(int zip){
        return 0;
    }

    // this will update the random by zip every 10 seconds. so first time when you call localhost:8080/weather/1 you got:71, continue reload, you still get 71 during 10 seconds. after 10 seconds, you reload again, you got another random number.
    @Scheduled(fixedRate = 10_000)
    public void update(){
        System.out.println("updating weather");
        IntStream.rangeClosed(1,5)
                .forEach(this.client::getWeatherInfo);
    }

}
