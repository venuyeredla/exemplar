package com.exemplar.kafka;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(havingValue = "true",name = "kafka.enable")
public class AsyncProducer {
	
	@Value(value = "${kafka.topic}")
    private String kafkaTopic;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void postMessage(String id) {
		
		String message= "First message from application";
		
		kafkaTemplate.send(kafkaTopic,message);
		
		ProducerRecord<String, String> producerRecord=new ProducerRecord<String, String>(kafkaTopic, id, message);
		
		CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(producerRecord);
		
		 completableFuture.whenComplete((result, ex) -> {
			   if (Objects.isNull(ex)) {
				   System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			   }else {
				   System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
			   }
			   
		   });
		
		
		//log.info("sent to kafka");
	}
	
	

}
