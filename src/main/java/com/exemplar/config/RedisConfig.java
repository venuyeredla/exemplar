package com.exemplar.config;

public class RedisConfig {

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
