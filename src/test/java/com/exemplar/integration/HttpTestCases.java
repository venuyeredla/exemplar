package com.exemplar.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.exemplar.controllers.KafkaMsg;
import com.exemplar.data.UserData;
import com.exemplar.entity.User;
import com.exemplar.util.AppConstants;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpTestCases {

	private static String BASE="http://localhost:";
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	

	@Test
	@Disabled
	void applicationBase() throws Exception {
		String url=BASE + port + "/api/";
		Map<String,String> response = this.restTemplate.getForObject(url, Map.class);
		assertNotNull(response);
	}
	
	
	@Test
	@Disabled
	void testCreateUser() throws Exception {
		String url=BASE + port + "/api/v1/user/create";
	
		User user = UserData.getUser();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AppConstants.JWT_TOKEN);
	//	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		
		HttpEntity<User> request = new HttpEntity<>(user, headers);
		User postForObject = restTemplate.postForObject(url, request, User.class);	
		System.out.println(postForObject);
	//	ResponseEntity<User> response = restTemplate.postForEntity(url, request , User.class);
	//	assertEquals(HttpStatusCode.valueOf(HttpStatus.SC_SUCCESS), response.getStatusCode());
		
	}
	
	@Test
	@Disabled
	public void getuserInfor() throws Exception {
		
			String url=BASE + port + "/api/v1/user/ratan@ecom.com";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(AppConstants.JWT_TOKEN);
			HttpEntity<Object> request = new HttpEntity<>(headers);
			
			ResponseEntity<User> exchange = this.restTemplate.exchange(url, HttpMethod.GET, request, User.class);
			
			assertNotNull(exchange);
		}
	
	@Test
	@Disabled
	void postToKafka() throws Exception {
		
		String url=BASE + port + "/api/kafka/post";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AppConstants.JWT_TOKEN);
	//	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		KafkaMsg kafkaMsg=new KafkaMsg();
		kafkaMsg.setMsg("venugopal reddy and venguopal");
		
		HttpEntity<KafkaMsg> request = new HttpEntity<>(kafkaMsg, headers);
		Boolean postForObject = restTemplate.postForObject(url, request, Boolean.class);	
		System.out.println(postForObject);
	//	ResponseEntity<User> response = restTemplate.postForEntity(url, request , User.class);
	//	assertEquals(HttpStatusCode.valueOf(HttpStatus.SC_SUCCESS), response.getStatusCode());
		
	}
	
	@Test
	public void getWordCount() throws Exception {
		
			String url=BASE + port + "/api/kafka/count/venugopal";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(AppConstants.JWT_TOKEN);
			HttpEntity<Object> request = new HttpEntity<>(headers);
			
			ResponseEntity<Long> exchange = this.restTemplate.exchange(url, HttpMethod.GET, request, Long.class);
			
			System.out.println(exchange.getBody());
	}
}	