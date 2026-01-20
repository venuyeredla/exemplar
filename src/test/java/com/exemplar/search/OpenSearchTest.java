package com.exemplar.search;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.opensearch.client.opensearch._types.OpenSearchException;

public class OpenSearchTest {
	
	
	@Test
	public void createIndeces() {
		OpenSearchService openSearchService=new OpenSearchService();
		try {
			openSearchService.init();
			//openSearchService.createIndex();
			openSearchService.indexDocment();
			
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | OpenSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
