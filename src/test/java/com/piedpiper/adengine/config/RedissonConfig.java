package com.piedpiper.adengine.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

public class RedissonConfig {
	
	private RedissonClient client;
	
	public RedissonClient getClient() {
		if (client == null) {
			var config = new Config();
			config.useSingleServer()
			.setAddress("redis://127.0.0.1:6379");
			client = Redisson.create(config);
		}
		
		return client;
	}
	
	public RedissonReactiveClient getRxClient() {
		return getClient().reactive();
	}

}
