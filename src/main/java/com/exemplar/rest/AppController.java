package com.exemplar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pilot.entity.Item;
import com.pilot.service.ItemService;

@RestController
@RequestMapping("/api")
public class AppController {
    @RequestMapping
    public String getAppInfo(){
    return "Exemplar Application";
    }
    @RequestMapping("appName")
    public String getAppName(){
        return "Exemplar Application";
    }
    
    
    @Autowired
	private ItemService itemService;
	
	@GetMapping("/")
	public String root() {
		return "Spring boot -sample application";
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
