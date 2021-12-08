package com.piedpiper.adengine;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BucketAsMapTest extends BaseTest{
	
	@Test
	public void keyValueBucketAsMapTest() {
		//in the below we are trying to get the values of multiple keys
		//as a map of key/val pairs.
		//so we should have set these keys in advance to get them as a map of key/val pairs
		Mono<Void> mapBuckets= this.client.getBuckets(StringCodec.INSTANCE)
		.get("campaign:fedex:all", "campaign:nile:all", "campaign:surf:all", "campaign:trel:all")
		.doOnNext(System.out::println)
		.then();
		
		StepVerifier.create(mapBuckets).verifyComplete();
	}

}
