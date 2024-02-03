package com.exemplar.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;


@Service
@ConditionalOnProperty(havingValue = "true",name = "enable.kafka")
public class EventDriverService {
	
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private String topicName;
	
	public void postMessage(String id) {
		
		kafkaTemplate.send("pilot", "First message from application");
		
		//log.info("sent to kafka");
	}
	
	
	public void sendMessageWithCallBack(String message) {
	     ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
	   /*  future.whenComplete((result, ex) -> {
	         if (ex == null) {
	             System.out.println("Sent message=[" + message + 
	                 "] with offset=[" + result.getRecordMetadata().offset() + "]");
	         } else {
	             System.out.println("Unable to send message=[" + 
	                 message + "] due to : " + ex.getMessage());
	         }
	     }); */
	}
	
	/**
	 * We can implement multiple listeners for a topic, each with a different group Id. Furthermore, one consumer can listen for messages from various topics:
	 * @param message
	 */

	@KafkaListener(topics = "topicName", groupId = "foo")
	//@KafkaListener(topics = "topic1, topic2", groupId = "foo")
	public void listenGroupFoo(String message) {
	    System.out.println("Received Message in group foo: " + message);
	}
	
	
	@KafkaListener(topics = "topicName")
	public void listenWithHeaders(
	  @Payload String message, 
	  @Header(KafkaHeaders.NATIVE_HEADERS) int partition) {
	      System.out.println(
	        "Received Message: " + message+ "from partition: " + partition);
	}

	
	/**
	 * Listening from specific partition.
	 * @param message
	 * @param partition
	 */
	@KafkaListener(
			  topicPartitions = @TopicPartition(topic = "topicName",
			  partitionOffsets = {
			    @PartitionOffset(partition = "0", initialOffset = "0"), 
			    @PartitionOffset(partition = "3", initialOffset = "0")}),
			  containerFactory = "partitionsKafkaListenerContainerFactory")
			public void listenToPartition(
			  @Payload String message, 
			  @Header(KafkaHeaders.PARTITION) int partition) {
			      System.out.println(
			        "Received Message: " + message+ "from partition: " + partition);
			}
	
	@KafkaListener(
			  topics = "topicName", 
			  containerFactory = "filterKafkaListenerContainerFactory")
			public void listenWithFilter(String message) {
			    System.out.println("Received Message in filtered listener: " + message);
			}
	
	
}
