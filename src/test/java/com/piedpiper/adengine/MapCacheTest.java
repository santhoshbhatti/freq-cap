package com.piedpiper.adengine;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.piedpiper.adengine.dto.Campaign;
import com.piedpiper.adengine.dto.LineItem;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MapCacheTest extends BaseTest{

	@Test
	public void mapCacheTest() {
		//in redis we can set ttl or expiry for keys when
		//we set the keys using "set" commng
		//But we cannot sert the expiry for keys in a map
		
		//So Redisson provides a way for setting expiry on keys of a redis map
		//through the mapCache data structure
		
		RMapCacheReactive<Object, Object> mapCache = this
				.client
				.getMapCache("user:123-432-908-23456", new TypedJsonJacksonCodec(String.class,Campaign.class));
		var lineItems = List.of(new LineItem("fedex1",20,30),
				new LineItem("fedex2",50,10));
		var campaign = new Campaign("fedex",50,20,lineItems);
		System.out.println("campaign:"+campaign.getName());
		Mono<Object> mono=mapCache.put("campaign:"+campaign.getName(), campaign,20,TimeUnit.SECONDS);
		Mono<Object> mono2 = mapCache.get("campaign:"+campaign.getName());
		var afterGet=mono2.doOnNext(ele -> System.out.println(ele)).then();
		StepVerifier.create(mono.concatWith(afterGet).then()).verifyComplete();
		
		sleep(10);
		mono2 = mapCache.get("campaign:"+campaign.getName());
		mono2.doOnNext(ele -> System.out.println(ele)).subscribe();
		
		sleep(5);
		mono2 = mapCache.get("campaign:"+campaign.getName());
		mono2.doOnNext(ele -> System.out.println(ele)).subscribe();
		
	}
}
