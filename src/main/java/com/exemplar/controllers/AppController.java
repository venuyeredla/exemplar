package com.exemplar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemplar.entity.Item;
import com.exemplar.service.ItemService;

@RestController
@RequestMapping("/api")
public class AppController {
	
    @Autowired
	private ItemService itemService;
	
	@GetMapping("/me")
	public String root() {
		return "Exemplar Application";
	}
	
	@PostMapping("/save")
	public String save(@RequestBody Item item) {
	   itemService.save(item);
		return "sucess";
	}
	
	@PostMapping("/update")
	public String update(@RequestBody Item item) {
		try {
			itemService.updateAccountViaDistributedLocks(item.getId());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		return "sucess";
	}
	
	@GetMapping("/get/{id}")
	public Item get(@PathVariable("id") long id) {
		// return itemService.get(id);
		return new Item();
	}
	

}
