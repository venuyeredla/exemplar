package com.exemplar.cache;

public class RedisService {
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
	
	@Autowired
	RedisRepo redisRepo;
	
	@Test
	public void testRedis() {
		
		JedisPool jedisPool= new JedisPool(new JedisPoolConfig(), "vdpsidxtst04",6379 , 3000, "change1t",9);
	
		JedisConnectionFactory jFac=new JedisConnectionFactory(new JedisPoolConfig());
		
		
		  try (Jedis jedis = jedisPool.getResource()) {

	            String customerPartyNumber = jedis.get("oid.pid.41279");

	            System.out.println(customerPartyNumber);

	        }
		 
	}
	
  */
}


