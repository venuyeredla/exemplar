package com.exemplar.cache;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exemplar.entity.RedisUser;

@Service
public class RedisService {
	
	@Autowired
	private UserRedisRepo  userRedisRepo;

	public void keepUserInCache(RedisUser user) {
		userRedisRepo.save(user);
	}
	
	public RedisUser getUserFromCache(String userid) {
	  Optional<RedisUser> optional=	userRedisRepo.findById(userid);
	  return optional.orElseGet(()->{return null;});
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