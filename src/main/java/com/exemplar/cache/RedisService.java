package com.exemplar.cache;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.exemplar.entity.Item;
import com.exemplar.entity.RedisUser;

@Service
public class RedisService {
	
	@Autowired
	private UserRedisRepo  userRedisRepo;
	
	@Autowired
	@Qualifier("itemCacheTemplate")
	RedisTemplate<Long, Item>  itemCacheTemplate;

	public void keepUserInCache(RedisUser user) {
		userRedisRepo.save(user);
	}
	
	public RedisUser getUserFromCache(String userid) {
	  Optional<RedisUser> optional=	userRedisRepo.findById(userid);
	  return optional.orElseGet(()->{return null;});
	}
	
	public void keepItemInCache() {
		
		Long longKey=2000L;
		Item item=new Item();
		item.setId(longKey);
		item.setBalance(longKey);
		
		itemCacheTemplate.opsForValue().set(longKey, item);
		
		
		
	}
	
	public Item getItemFromCache(Long longKey) {
		longKey=2000L;
		Item cahcedResult = itemCacheTemplate.opsForValue().get(longKey);
		return cahcedResult;
	}
	
	

	

}

/*
@Qualifier
 @Autowired
 private ExpirableLockRegistry lockRegistry;

 public boolean update(String request) {
     Lock lock =  lockRegistry.obtain(request);
     boolean success = lock.tryLock();

     if (!success) {
         return false;
     }
     
     // ...
     // update a shared resource  
     // ... 
     
     lock.unlock();
     return true;
 }

*/