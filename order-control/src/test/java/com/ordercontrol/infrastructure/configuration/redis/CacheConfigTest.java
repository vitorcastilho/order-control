package com.ordercontrol.infrastructure.configuration.redis;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@DisplayName("CacheConfig")
class CacheConfigTest {

	@Test
	@DisplayName("expõe um CacheManager baseado em Redis")
	void buildsRedisCacheManager() {
		RedisConnectionFactory connectionFactory = new LettuceConnectionFactory();

		CacheManager cacheManager = new CacheConfig().cacheManager(connectionFactory);

		assertNotNull(cacheManager);
		assertInstanceOf(RedisCacheManager.class, cacheManager);
	}
}
