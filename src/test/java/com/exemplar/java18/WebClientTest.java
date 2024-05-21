package com.exemplar.java18;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.Test;

public class WebClientTest {

	
	@Test
	public void testHTTPURL() {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					  .uri(new URI("http://localhost:2024/app/api/me"))
					  //.headers("key1", "value1", "key2", "value2")
					  //.timeout(Duration.of(10, SECONDS))
					  .GET()
					  .build();
			HttpClient newHttpClient = HttpClient.newHttpClient();
			HttpResponse<String> response = newHttpClient.send(request, BodyHandlers.ofString());
			
			if (response.statusCode()!=200) {
				assertFalse(true, "Invalid status code");
			}
			System.out.println("Response is : " + response.body());
			
			
		} catch (URISyntaxException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
