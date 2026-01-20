package com.exemplar.kafka;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnProperty(havingValue = "true",name = "kafka.enable")
@EnableKafka
@Slf4j
public class KafkaPublisher {
	
	@Value(value = "${kafka.topic}")
    private String kafkaTopic;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void postMessage(String id) {
		
		 try {
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		String key="kafka-first-1";
		String message= "First message from application";
		
		CompletableFuture<SendResult<String, String>> asyncResp = kafkaTemplate.send(kafkaTopic, key, message);
		asyncResp.whenComplete((result, ex) -> {
			   if (Objects.isNull(ex)) {
				   System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			   }else {
				   System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
			   }
		 });
		
		String key2="kafka-first-2";
		String message2= "First message from application - 2";
		
		ProducerRecord<String, String> producerRecord=new ProducerRecord<String, String>(kafkaTopic,key2, message2);
		
		CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(producerRecord);
		
		 completableFuture.whenComplete((result, ex) -> {
			   if (Objects.isNull(ex)) {
				   System.out.println("Sent record message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			   }else {
				   System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
			   }
		   });
		 
		 
			
		 try {
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}
	
	
	  public void postTostreamInput(String message) {
	        kafkaTemplate.send("input-topic", message)
	            .whenComplete((result, ex) -> {
	                if (ex == null) {
	                    log.info("Message sent to topic: {}", message);
	                } else {
	                    log.error("Failed to send message", ex);
	                }
	            });
	    }
	
	
	
	

}
