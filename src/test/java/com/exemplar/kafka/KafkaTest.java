package com.exemplar.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("dev")
public class KafkaTest {
	
	@Autowired
	KafkaPublisher kafkaPublisher;
	
	
	@Test
	public void testKafkaPubSub() {
		kafkaPublisher.postMessage("xyx");
		
	}
}
