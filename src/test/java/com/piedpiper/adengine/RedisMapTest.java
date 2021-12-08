package com.piedpiper.adengine;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.piedpiper.adengine.dto.Campaign;
import com.piedpiper.adengine.dto.LineItem;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedisMapTest extends BaseTest{
	
	@Test
	public void redisMapTest() {
		//user id map
		//contains key as campaign names and values are freq count
		//the freq at campaign level will be stored with key campaign:campName:All
		RMapReactive<Object, Object> userCampaigns = this.client.getMap("user:1234-8763-7893",StringCodec.INSTANCE);
		Mono<Object> putAll = userCampaigns.put("campaign:fedex:all", 34);
		Mono<Object> put1 = userCampaigns.put("campaign:fedex:1", 10);
		Mono<Object> put2 = userCampaigns.put("campaign:fedex:2", 14);
		Mono<Object> put3 = userCampaigns.put("campaign:fedex:3", 5);
		Mono<Object> put4 = userCampaigns.put("campaign:fedex:4", 5);
		
		StepVerifier.create(putAll.concatWith(put1).concatWith(put2).concatWith(put3).concatWith(put4).then()).verifyComplete();
		
	}

	@Test
	public void redisMapTestFromJavaMap() {
		RMapReactive<Object, Object> userCampaigns = this.client.getMap("user:1234-8763-7894",StringCodec.INSTANCE);
		var campaignFreqMap=Map.of("campaign:fedex:all", 34,
				"campaign:fedex:1", 10,
				"campaign:fedex:2", 14,
				"campaign:fedex:3", 5,
				"campaign:fedex:4", 5);
		
		Mono<Void> putAll = userCampaigns.putAll(campaignFreqMap);
		StepVerifier.create(putAll).verifyComplete();
		
	}
	
	@Test
	public void redisMapTestFromJavaObjects() {
		RMapReactive<Object, Object> userCampaigns =
				this.client.getMap("campaign",new TypedJsonJacksonCodec(String.class,Campaign.class));
		var lineItems = List.of(new LineItem("fedex1",20,30),
				new LineItem("fedex2",50,10));
		var campaign = new Campaign("fedex",50,20,lineItems);
				
		var campaignMap=Map.of("campaign:"+campaign.getName(),campaign);
		
		Mono<Void> putAll = userCampaigns.putAll(campaignMap);
		StepVerifier.create(putAll).verifyComplete();
		
	}
}
