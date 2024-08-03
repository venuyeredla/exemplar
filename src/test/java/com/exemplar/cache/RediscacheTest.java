package com.exemplar.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.exemplar.entity.RedisUser;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest (classes = RedisTestConfiguration.class)
//@Import(RedisTestConfiguration.class)
//@TestPropertySource(locations="classpath:test.properties")
//@ActiveProfiles("dev")
public class RediscacheTest {
	
	@Autowired
	RedisService redisService;
	
	//@Test
	public void testRedisConnection() {
		 JedisPool pool = new JedisPool("localhost", 6379);
		 try (Jedis jedis = pool.getResource()) {
			 // jedis.set("clientName", "Jedis");
			  String string = jedis.get("user-2");
			  System.out.println("Cache value :"+string);
			}
		 
		 pool.close();
	 }
	
	@Test
	public void saveAndGetUserTest() {
		
		/*RedisUser user=new RedisUser();
		
		user.setId("venugopal@ecom.com");
		user.setFirstName("venugopal");
		user.setLastName("gopal");
		user.setEmail("venugopal@ecom.com");
		user.setPassword("xasdfasf");
		
		redisService.keepUserInCache(user); */
		
		
		RedisUser cached = redisService.getUserFromCache("venugopal@ecom.com");
		System.out.println(cached.getFirstName());
		
	}
	
	
}


