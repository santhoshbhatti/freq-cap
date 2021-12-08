package com.piedpiper.adengine;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.piedpiper.adengine.config.RedissonConfig;
import com.piedpiper.adengine.dto.Campaign;
import com.piedpiper.adengine.dto.LineItem;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class RedissonLocalCachedMapTest extends BaseTest{
	
	private RLocalCachedMap<Integer, Campaign> localCachedMap ;
	@BeforeAll
	public void setupClient() {
		RedissonConfig config = new RedissonConfig();
		RedissonClient client = config.getClient();
		LocalCachedMapOptions<Integer, Campaign> mapOptions = LocalCachedMapOptions.<Integer, Campaign>defaults()
				.syncStrategy(SyncStrategy.UPDATE)
				.reconnectionStrategy(ReconnectionStrategy.NONE);
		
		this.localCachedMap = client.
				getLocalCachedMap("camaign",new TypedJsonJacksonCodec(Integer.class,Campaign.class), mapOptions);
		
		
	}
	
	@Test
	public void localCacheInsertTest() {
		
		var lineItems = List.of(new LineItem("fedex1",20,30),
				new LineItem("fedex2",50,10));
		var campaign1 = new Campaign("fedex",50,20,lineItems);
		
		var lineItems2 = List.of(new LineItem("xerox1",20,30),
				new LineItem("xerox2",50,10));
		var campaign2 = new Campaign("xerox344",100,50,lineItems2);
		
		localCachedMap.put(1, campaign1);
		localCachedMap.put(2, campaign2);
		System.out.println("Thread name >>> "+Thread.currentThread().getName());
		Flux.interval(Duration.ofSeconds(1L))
		.doOnNext(i -> System.out.println(i +"  "+Thread.currentThread().getName()+Thread.currentThread().isDaemon()+"==========>"+localCachedMap.get(1)))
		.subscribe();
		
		sleep(300);
		
	}
	
	@Test
	public void pushChangesToMapTest() {
		var lineItems = List.of(new LineItem("fedex1",20,30),
				new LineItem("fedex2",50,10));
		var campaign1 = new Campaign("blue-dart",50,20,lineItems);
		localCachedMap.put(1, campaign1);
	}
}
