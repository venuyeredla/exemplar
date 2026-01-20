package com.exemplar.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.exemplar.entity.Item;

@Service
public class RedisMessagePublisher {
	
	@Autowired
	@Qualifier("itemCacheTemplate")
	RedisTemplate<Long, Item>  itemCacheTemplate;
	
	@Autowired
	private ChannelTopic topic;

	public RedisMessagePublisher() {
	 
	}

	public void publish(String message) {
	    	
	   Long longKey=2000L;
	   Item item=new Item();
	   item.setId(longKey);
	   item.setBalance(longKey);
	   Long convertAndSend = itemCacheTemplate.convertAndSend(topic.getTopic(), item);
	   System.out.println("Published successfull.  "+ convertAndSend);
	   
	   
	   
	    }

}
