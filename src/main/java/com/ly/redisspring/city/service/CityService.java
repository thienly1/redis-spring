package com.ly.redisspring.city.service;

import com.ly.redisspring.city.client.CityClient;
import com.ly.redisspring.city.dto.City;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CityService {

    @Autowired
    private CityClient cityClient;
    private RMapReactive<String, City> cityMap;

    public  CityService(RedissonReactiveClient client){
        this.cityMap = client.getMap("city", new TypedJsonJacksonCodec(String.class, City.class));
    }

    /*
    * get from cache, if empty -get from db /source
    * put it in cache
    * return
    */
    public Mono<City> getCity(final String zipCode){
        return this.cityMap.get(zipCode)  //get from cache
                .switchIfEmpty(this.cityClient.getCity(zipCode)) //if empty, switch to source to get
                .flatMap(c -> this.cityMap.fastPut(zipCode, c) //put it in cache
                        .thenReturn(c)                         //return value
                );
    }
}
