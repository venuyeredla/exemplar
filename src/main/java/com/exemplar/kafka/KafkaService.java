package com.exemplar.kafka;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Service
@ConditionalOnProperty(havingValue = "true",name = "kafka.enable")
public class KafkaService {
	
	@Value(value = "${kafka.topic}")
    private String kafkaTopic;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void postMessage(String id) {
		
		String message= "First message from application";
		
		kafkaTemplate.send(kafkaTopic,message);
		
		ProducerRecord<String, String> producerRecord=new ProducerRecord<String, String>(kafkaTopic, id, message);
		
		CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(producerRecord);
		
		
		//log.info("sent to kafka");
	}
	
	
	public void sendMessageWithCallBack(String message) {
	   CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(kafkaTopic, message);
	   completableFuture.whenComplete((result, ex) -> {
		   if (Objects.isNull(ex)) {
			   System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
		   }else {
			   System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
		   }
		   
	   });
	 
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
			  @Header(KafkaHeaders.ACKNOWLEDGMENT) int partition) {
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