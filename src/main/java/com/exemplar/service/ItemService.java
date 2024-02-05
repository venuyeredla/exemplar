package com.exemplar.service;


import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import com.exemplar.jpa.ItemRepository;

import com.exemplar.entity.Item;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemService {
	
		@Autowired
	 	private ItemRepository itemRepository;
	    private  ReentrantLock rlock = new ReentrantLock();
	    
	    @Autowired
	    private  LockRegistry lockRegistry;
	  //  private JdbcTemplate jdbcTemplate;
	    
	    
	    public void updateAccount(Long id) throws InterruptedException {
	       boolean lockAquired =  rlock.tryLock();
	       if(lockAquired){
	           try{
	               log.info("lock taken");
	               Optional<Item> item = itemRepository.findById(id);
	             //  account.setBalance(account.getBalance() + 100L);
	               Thread.sleep(20_000);
	               itemRepository.save(item.get());
	           }
	           finally {
	               rlock.unlock();
	           }
	       }
	    }
	    
	   



	    public void updateAccountViaDistributedLocks(Long id) throws InterruptedException {
	    	Lock obtain = lockRegistry.obtain(String.valueOf(id));
	        boolean lockAquired =  obtain.tryLock();
	        if(lockAquired){
	            try{
	               // log.info("lock taken");
	               Optional<Item> account = itemRepository.findById(id);
	               account.get().setBalance(30000L);
	                //account.setBalance(account.getBalance() + 100L);
	                Thread.sleep(20_000);
	                itemRepository.save(account.get());
	            }
	            finally {
	            	obtain.unlock();
	            }
	        }
	    }

		public Item get(long id) {
			// TODO Auto-generated method stub
			return itemRepository.findById(id).get();
		}

		public void save(Item item) {
			itemRepository.save(item);
			
		}



}
