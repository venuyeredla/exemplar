package com.exemplar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemplar.kafka.EventDriverService;

@RestController
@ConditionalOnProperty(havingValue = "true",name = "enable.kafka")
public class KafkaController {
	
	@Autowired
	private EventDriverService kafkaService;
	
	@GetMapping("/postk")
	public boolean post() {
		// return itemService.get(id);
		
		kafkaService.postMessage(null);
		
		return true;
	}

}
