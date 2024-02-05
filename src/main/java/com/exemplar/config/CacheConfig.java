package com.exemplar.config;

import java.awt.print.Book;
import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import io.lettuce.core.RedisClient;

//@Configuration
public class CacheConfig {
	
	
	public static RedisClient getInstance(String ip, final int port) {
        
        return null;
    }
	
	
	@Bean
	public RedisTemplate<Long, Book> redisTemplate(RedisConnectionFactory connectionFactory) {
	    RedisTemplate<Long, Book> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    // Add some specific configuration here. Key serializers, etc.
	    return template;
	}
	
	
	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
	    return RedisCacheConfiguration.defaultCacheConfig()
	      .entryTtl(Duration.ofMinutes(60))
	      .disableCachingNullValues()
	      .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}
	
	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
	    return (builder) -> builder
	      .withCacheConfiguration("itemCache",
	        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
	      .withCacheConfiguration("customerCache",
	        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
	}

}
