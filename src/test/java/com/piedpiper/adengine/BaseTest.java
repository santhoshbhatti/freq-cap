package com.piedpiper.adengine;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

import com.piedpiper.adengine.config.RedissonConfig;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
	private final RedissonConfig config = new RedissonConfig();
	
	protected RedissonReactiveClient client;
	
	@BeforeAll
	public void setClient() {
		client = config.getRxClient();
	}
	
	@AfterAll
	public void shutdown() {
		client.shutdown();
	}
	
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
