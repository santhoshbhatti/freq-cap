package com.piedpiper.adengine;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedisEventHandlerTest extends BaseTest{
	

	@Test
	public void expiredEventHandlerTest() {
		
		RBucketReactive<Object> bucket = this.client.getBucket("campaign:buds:all",StringCodec.INSTANCE);
		Mono<Void> set = bucket.set(300, 10, TimeUnit.SECONDS);
		var get = bucket.get().doOnNext(System.out::println).then();
		Mono<Void> expiredEvent = bucket.addListener(new ExpiredObjectListener() {
			
			@Override
			public void onExpired(String name) {
				System.out.println("expired >>>> : "+name);
				
			}
		}).then();
		StepVerifier.create(set.concatWith(get).concatWith(expiredEvent)).verifyComplete();
		sleep(15);
	}
	@Test
	public void deleteEventHandlerTest() {
		
		RBucketReactive<Object> bucket = this.client.getBucket("campaign:tube:all",StringCodec.INSTANCE);
		Mono<Void> set = bucket.set(300);
		var get = bucket.get().doOnNext(System.out::println).then();
		Mono<Void> deletedEvent = bucket.addListener(new DeletedObjectListener() {
			
			@Override
			public void onDeleted(String name) {
				System.out.println("deleted >>>>>>!!!!! "+name);
				
			}
		}).then();
		StepVerifier.create(set.concatWith(get).concatWith(deletedEvent)).verifyComplete();
		sleep(60);
	}
}
