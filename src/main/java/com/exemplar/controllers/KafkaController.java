package com.exemplar.controllers;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemplar.kafka.KafkaPublisher;

@RestController
//@ConditionalOnProperty(havingValue = "true",name = "enable.kafka")
@RequestMapping("/kafka")
public class KafkaController {
	
	@Autowired
	private KafkaPublisher asyncProducer;
	
	@Autowired
	private StreamsBuilderFactoryBean factoryBean;

	
	@PostMapping("/post")
	public boolean post(@RequestBody KafkaMsg kafkaMsg) {
		// return itemService.get(id);
		
		asyncProducer.postTostreamInput(kafkaMsg.getMsg());
		
		return true;
	}

		
	@GetMapping("/count/{word}")
	public Long getWordCount(@PathVariable String word) {
	    KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
	    ReadOnlyKeyValueStore<String, Long> counts = kafkaStreams.store(
	      StoreQueryParameters.fromNameAndType("counts", QueryableStoreTypes.keyValueStore())
	    );
	    return counts.get(word);
	}
	
}
