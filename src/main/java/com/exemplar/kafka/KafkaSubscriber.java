package com.exemplar.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(havingValue = "true",name = "kafka.enable")
public class KafkaSubscriber {
	
	
	/**
	 * We can implement multiple listeners for a topic, each with a different group Id. Furthermore, one consumer can listen for messages from various topics:
	 * @param message
	 */
	
	
//	@KafkaListener(
//			  topicPartitions = @TopicPartition(topic = "ecomtopic",
//			  partitionOffsets = {
//			    @PartitionOffset(partition = "0", initialOffset = "0")}))
//	public void firstSubscriber(String message) {
//	    System.out.println("Received Message in group foo: " + message);
//	}
	
	

	@KafkaListener(topics = "ecomtopic", groupId = "ecomgroup")
	public void firstSubscriber(String message) {
	    System.out.println("Received Message in group foo: " + message);
	}
	
	
	//@KafkaListener(topics = "topicName")
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
	 /*@KafkaListener(
			  topicPartitions = @TopicPartition(topic = "topicName",
			  partitionOffsets = {
			    @PartitionOffset(partition = "0", initialOffset = "0"), 
			    @PartitionOffset(partition = "3", initialOffset = "0")}),
			  containerFactory = "partitionsKafkaListenerContainerFactory")
			  */
	public void listenToPartition(
	  @Payload String message, 
	  @Header(KafkaHeaders.ACKNOWLEDGMENT) int partition) {
	      System.out.println(
	        "Received Message: " + message+ "from partition: " + partition);
	}
	
	
 /*
	@KafkaListener(
			  topics = "topicName", 
			  containerFactory = "filterKafkaListenerContainerFactory")
			  ]
*/
			public void listenWithFilter(String message) {
			    System.out.println("Received Message in filtered listener: " + message);
			}
	
	
}
