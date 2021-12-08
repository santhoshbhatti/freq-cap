package com.piedpiper.adengine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ListTest extends BaseTest{
	
	@Test
	public void listTest() {
		RListReactive<Object> list = this.client.getList("number-input",LongCodec.INSTANCE);
		List<Long> randomList=LongStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
		StepVerifier.create(list.addAll(randomList).then()).verifyComplete();
		StepVerifier.create(list.size()).expectNext(10).verifyComplete();
		
	}
	
	@Test
	public void queueTest() {
		RQueueReactive<Long> queue = this.client.getQueue("number-input", LongCodec.INSTANCE);
		
		Mono<Void> queuePoll=queue.poll().repeat(3).doOnNext(System.out::println).then();
		StepVerifier.create(queuePoll).verifyComplete();
		StepVerifier.create(queue.size()).expectNext(6).verifyComplete();
		
	}
	
	@Test
	public void dequeueTest() {
		RDequeReactive<Object> deque = this.client.getDeque("number-input", LongCodec.INSTANCE);
		
		Mono<Void> queuePoll=deque.pollLast().repeat(3).doOnNext(System.out::println).then();
		StepVerifier.create(queuePoll).verifyComplete();
		StepVerifier.create(deque.size()).expectNext(2).verifyComplete();
		
	}

}
