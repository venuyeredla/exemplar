package com.exemplar.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.exemplar.jpa.ItemRepository;

@Service
public class CachableService {
	
	ItemRepository itemRepository;

	@Cacheable(value = "itemCache")
	public Item getItemForId(String id) {
		/*
		 * return itemRepository.findById(id) .orElseThrow(RuntimeException::new);
		 */
		return null;
	}
}

class Item {
	
}