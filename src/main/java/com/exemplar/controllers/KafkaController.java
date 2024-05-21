package com.exemplar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemplar.kafka.AsyncProducer;

@RestController
@ConditionalOnProperty(havingValue = "true",name = "enable.kafka")
public class KafkaController {
	
	@Autowired
	private AsyncProducer asyncProducer;
	
	@GetMapping("/postk")
	public boolean post() {
		// return itemService.get(id);
		
		asyncProducer.postMessage("");
		
		return true;
	}

}
