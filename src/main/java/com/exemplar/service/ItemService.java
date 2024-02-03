package com.exemplar.service;


import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import com.exemplar.*;
import com.exemplar.jpa.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemService {
	
	@Autowired
	 	private ItemRepository itemRepository;
	    private  ReentrantLock rlock = new ReentrantLock();
	    

	  //  private JdbcTemplate jdbcTemplate;
	    
	    
	    public void updateAccount(Long id) throws InterruptedException {
	       boolean lockAquired =  rlock.tryLock();
	       if(lockAquired){
	           try{
	              // log.info("lock taken");
	               Optional<Item> account = itemRepository.findById(id);
	             //  account.setBalance(account.getBalance() + 100L);
	               Thread.sleep(20_000);
	               itemRepository.save(account.get());
	           }
	           finally {
	               rlock.unlock();
	           }
	       }
	    }
	    
	   

	    @Autowired
	    private  LockRegistry lockRegistry;

	    public void updateAccountViaDistributedLocks(Long id) throws InterruptedException {
	        var lock = lockRegistry.obtain(String.valueOf(id));
	        boolean lockAquired =  lock.tryLock();
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
	                lock.unlock();
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
