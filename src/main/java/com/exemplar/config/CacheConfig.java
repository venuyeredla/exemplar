package com.exemplar.config;

import java.awt.print.Book;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableRedisRepositories
public class CacheConfig {
	
	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
	    return RedisCacheConfiguration.defaultCacheConfig()
	      .entryTtl(Duration.ofMinutes(60))
	      .disableCachingNullValues()
	      .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

	  @Bean
	  public RedisConnectionFactory redisConnectionFactory() {
	   /*   List<String> nodes = Arrays.asList(
	    			"localhost:6379"
	    		);
	   // RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(nodes);
	    */
	    RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration();
	    configuration.setHostName("localhost");
	    configuration.setPort(6379);
	    return new LettuceConnectionFactory(configuration);
	  }
	
	@Bean
	public RedisTemplate<Long, Book> redisTemplate(RedisConnectionFactory connectionFactory) {
	    RedisTemplate<Long, Book> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    // Add some specific configuration here. Key serializers, etc.
	    /*
	    template.opsForValue().set(null, null);
	    template.delete(120L);
	    template.opsForValue().get(120L);
	    */
	    return template;
	}
	


	
	//@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
	    return (builder) -> builder
	      .withCacheConfiguration("itemCache",
	        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
	      .withCacheConfiguration("customerCache",
	        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
	}

}
