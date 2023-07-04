package com.ly.redisspring.city.service;

import com.ly.redisspring.city.client.CityClient;
import com.ly.redisspring.city.dto.City;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityClient cityClient;
//    private RMapCacheReactive<String, City> cityMap;
    private RMapReactive<String, City> cityMap;

//    public  CityService(RedissonReactiveClient client){
//        this.cityMap = client.getMapCache("city", new TypedJsonJacksonCodec(String.class, City.class));
//    }
    public  CityService(RedissonReactiveClient client){
        this.cityMap = client.getMap("city", new TypedJsonJacksonCodec(String.class,  City.class));
    }

    /*
    * get from cache, if empty -get from db /source
    * put it in cache
    * return
    */
//    public Mono<City> getCity(final String zipCode){
//        return this.cityMap.get(zipCode)  //get from cache
//                .switchIfEmpty(this.cityClient.getCity(zipCode)) //if empty, switch to source to get
//                .flatMap(c -> this.cityMap.fastPut(zipCode, c, 10, TimeUnit.SECONDS) //put it in cache, then remove it after every 10 seconds
//                        .thenReturn(c)                         //return value
//                );
//    }
    public Mono<City> getCity(final String zipCode){
        return this.cityMap.get(zipCode)
                .onErrorResume(ex ->this.cityClient.getCity(zipCode)
                );
    }
    @Scheduled(fixedRate = 10_000)
    public void updateCity(){
        this.cityClient.getAll()
                .collectList()
                .map(list -> list.stream().collect(Collectors.toMap(City::getZip, Function.identity())))
                .flatMap(this.cityMap::putAll)
                .subscribe();

    }


}
