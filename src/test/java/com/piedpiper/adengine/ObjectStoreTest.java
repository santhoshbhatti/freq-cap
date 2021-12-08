package com.piedpiper.adengine;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.piedpiper.adengine.dto.Campaign;
import com.piedpiper.adengine.dto.LineItem;

import reactor.test.StepVerifier;

public class ObjectStoreTest extends BaseTest{

	@Test
	public void keyObjectStoreTest(){
		var campaign = new Campaign("fedex",20,10,null);
		//redis stores the campaign object in binary format.Not human readable: default codec
		RBucketReactive<Campaign> bucket = 
				this.client.getBucket("campaign:"+campaign.getName());
		
		var set = bucket.set(campaign);
		var get =bucket.get()
				.doOnNext(System.out::println)
				.then();

		StepVerifier.create(set.concatWith(get)).verifyComplete();
		
		
		
		
	}
	
	@Test
	public void keyObjectStoreTestJsonJacksonCodec(){
		var campaign = new Campaign("xerox",200,75,null);
		//redis stores the campaign object in JSON format. human readable. stores the fqcn as well as val for @class key in
		//serilized format. erroneous in case of class name changes ...cannot deserialize
		RBucketReactive<Campaign> bucket = 
				this.client.getBucket("campaign:"+campaign.getName(),JsonJacksonCodec.INSTANCE);
		
		var set = bucket.set(campaign);
		var get =bucket.get()
				.doOnNext(System.out::println)
				.then();

		StepVerifier.create(set.concatWith(get)).verifyComplete();
	}
	@Test
	public void keyObjectStoreTestTypedJsonJacksonCodec(){
		LineItem l1 = new LineItem("one", 10, 2);
		LineItem l2 = new LineItem("two", 9, 5);
		LineItem l3 = new LineItem("three", 11, 4);
		var campaign = new Campaign("Nike",45,23,List.of(l1,l2,l3));
		//redis stores the campaign object in JSON format. human readable. does not store the fqcn in serilized format. 
		RBucketReactive<Campaign> bucket = 
				this.client.getBucket("campaign:"+campaign.getName(),new TypedJsonJacksonCodec(Campaign.class));
		
		var set = bucket.set(campaign);
		var get =bucket.get()
				.doOnNext(System.out::println)
				.then();

		StepVerifier.create(set.concatWith(get)).verifyComplete();
	}
	
	@Test
	public void keyMapStore(){
		
		RMapReactive<String, Integer> map = 
				this.client.getMap("campaign:freq");
		
		var set = map.put("campaign:all", 100).then();
		Publisher<Void> get =map.get("campaign:all")
				.doOnNext(System.out::println)
				.then();

		StepVerifier.create(set.concatWith(get)).verifyComplete();
	}
	
	
}
