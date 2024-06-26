package com.exemplar.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@ConditionalOnProperty(havingValue = "true",name = "kafka.enable")
public class KafkaConfig {

		@Value(value = "${spring.kafka.bootstrap-servers}")
	    private String bootstrapAddress;
		
		@Value(value = "${kafka.topic}")
	    private String kafkaTopic;
		
	    private Object groupId;

	    @Bean
	    public KafkaAdmin kafkaAdmin() {
	        Map<String, Object> configs = new HashMap<>();
	        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
	        KafkaAdmin kafkaAdmin = new KafkaAdmin(configs);
	        
	      //  Map<String, TopicDescription> describeTopics = kafkaAdmin.describeTopics("topic1", "topic2");
	      //  Map<String, Object> configurationProperties = kafkaAdmin.getConfigurationProperties();
	      //  kafkaAdmin.setAutoCreate(false);
	        return kafkaAdmin;
	    }
	   
	    @Bean
	    public NewTopic topic1() {
	    	
	    	 NewTopic newTopic = new NewTopic(kafkaTopic, 1, (short) 1);	    	
	         return newTopic;
	    }
	    
	    @Bean
	    public ProducerFactory<String, String> producerFactory() {
	        Map<String, Object> configProps = new HashMap<>();
	        configProps.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
	        configProps.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	        configProps.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	        return new DefaultKafkaProducerFactory<>(configProps);
	    }

	    @Bean
	    public KafkaTemplate<String, String> kafkaTemplate() {
	        return new KafkaTemplate<>(producerFactory());
	    }
	    
	    @Bean
	    public ConsumerFactory<String, String> consumerFactory() {
	        Map<String, Object> props = new HashMap<>();
	        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
	        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
	        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class);
	
	        return new DefaultKafkaConsumerFactory<>(props);
	    }

	    @Bean
	    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
	        ConcurrentKafkaListenerContainerFactory<String, String> factory =  new ConcurrentKafkaListenerContainerFactory<>();
	        factory.setConsumerFactory(consumerFactory());
	        return factory;
	    }
	    
	 //   @Bean
		public ConcurrentKafkaListenerContainerFactory<String, String> filterKafkaListenerContainerFactory() {

		    ConcurrentKafkaListenerContainerFactory<String, String> factory =
		      new ConcurrentKafkaListenerContainerFactory<>();
		    factory.setConsumerFactory(consumerFactory());
		    factory.setRecordFilterStrategy(
		      record -> record.value().contains("World"));
		    return factory;
		}
	    
}
