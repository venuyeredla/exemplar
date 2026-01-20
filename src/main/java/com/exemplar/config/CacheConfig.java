package com.exemplar.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import com.exemplar.cache.RedisMessageSubscriber;
import com.exemplar.entity.Item;

@Configuration
@EnableRedisRepositories(basePackages = "com.exemplar.cache")
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
	
	@Bean("itemCacheTemplate")
	public RedisTemplate<Long, Item> redisItemTemplate(RedisConnectionFactory connectionFactory) {
	    RedisTemplate<Long, Item> redisItemTemplate = new RedisTemplate<>();
	    redisItemTemplate.setConnectionFactory(connectionFactory);
	    
	    //redisItemTemplate.setKeySerializer(null);
	    //redisItemTemplate.setValueSerializer(null);
	  
	    return redisItemTemplate;
	}
	
	@Bean
	ChannelTopic topic() {
	    return new ChannelTopic("messageQueue");
	}
	
	@Bean
	MessageListenerAdapter messageListener() { 
	    return new MessageListenerAdapter(new RedisMessageSubscriber());
	}

	@Bean
	RedisMessageListenerContainer redisContainer() {
	    RedisMessageListenerContainer container 
	      = new RedisMessageListenerContainer(); 
	    container.setConnectionFactory(redisConnectionFactory()); 
	    container.addMessageListener(messageListener(), topic()); 
	    return container; 
	}

	
	//@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
	    return (builder) -> builder
	      .withCacheConfiguration("itemCache",
	        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
	      .withCacheConfiguration("customerCache",
	        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
	}
	
	/*
	@Configuration
	public class RedisDistributedLockConfiguration {
	    private static final String LOCK_REGISTRY_REDIS_KEY = "MY_REDIS_KEY";
	    private  final Duration RELEASE_TIME_DURATION = Duration.ofSeconds(30);

	    @Bean
	    public ExpirableLockRegistry lockRegistry(RedisConnectionFactory redisConnectionFactory) {
	        RedisLockRegistry redisLockRegistry =
	            new RedisLockRegistry(redisConnectionFactory, LOCK_REGISTRY_REDIS_KEY,
	                RELEASE_TIME_DURATION.toMillis());
	        return redisLockRegistry;
	    }
	}
	*/

}
