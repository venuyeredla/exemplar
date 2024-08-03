package com.exemplar.cache;

import java.awt.print.Book;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@TestConfiguration
public class RedisTestConfiguration {

	//@Bean
	  public RedisConnectionFactory redisConnectionFactory() {
	    List<String> nodes = Arrays.asList(
	    			"localhost:6379"
	    		);
	    RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(nodes);
	    return new LettuceConnectionFactory(clusterConfig);
	  }
	
	//@Bean
	public RedisTemplate<Long, Book> redisTemplate(RedisConnectionFactory connectionFactory) {
	    RedisTemplate<Long, Book> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    // Add some specific configuration here. Key serializers, etc.
	    
	    template.opsForValue().set(null, null);
	    template.delete(120L);
	    template.opsForValue().get(120L);
	    
	    return template;
	}
	
	//@Bean
	public RedisService redisService() {
		return new RedisService();
	}
}
