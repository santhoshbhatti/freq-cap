package com.piedpiper.adengine;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class KeyValueNumberTest extends BaseTest{
	
	@Test
	public void keyValueIncrTest() {
		//if we want to do an incr/decr of a value associated with a key
		//we cannot use the previous bucket semantics instead we have 
		//the atomic long or atomic double types in redisson which closely mimic the
		//jdk AtomicLong and Atomic double respectively
		RAtomicLongReactive atomicLong = this.client.getAtomicLong("campaign:all");
		Mono<Void> incrOp=Flux.range(1, 30)
		.delayElements(Duration.ofSeconds(1))
		.flatMap(i -> atomicLong.incrementAndGet())
		.then();
		
		StepVerifier.create(incrOp).verifyComplete();
		
	}

}
