package com.piedpiper.adengine;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class SimpleKeyValueTest extends BaseTest{
	
	@Test
	public void keyValueAccessTest() {
		RBucketReactive<String> bucket= this.client
				.getBucket("user:1:name",StringCodec.INSTANCE);
		Mono<Void> set = bucket.set("abhigya");
		Mono<Void>  get = bucket.get().doOnNext(System.out::println).then();
		StepVerifier.create(set.concatWith(get)).verifyComplete();
	}
	
	@Test
	public void keyValueExpiryTest() {
		RBucketReactive<String> bucket= this.client
				.getBucket("user:1:name",StringCodec.INSTANCE);
		Mono<Void> set = bucket.set("abhigya", 15,TimeUnit.SECONDS);
		Mono<Void>  get = bucket.get().doOnNext(System.out::println).then();
		StepVerifier.create(set.concatWith(get)).verifyComplete();
	}
	
	@Test
	public void keyValueExpiryExtendTest() {
		RBucketReactive<String> bucket= this.client
				.getBucket("user:1:name",StringCodec.INSTANCE);
		Mono<Void> set = bucket.set("abhigya", 15,TimeUnit.SECONDS);
		Mono<Void>  get = bucket.get().doOnNext(System.out::println).then();
		StepVerifier.create(set.concatWith(get)).verifyComplete();
		
		sleep(10);
		
		var boolMono=bucket.expire(60,TimeUnit.SECONDS);
		StepVerifier.create(boolMono).expectNext(true)
		.verifyComplete();
		
	}

}
