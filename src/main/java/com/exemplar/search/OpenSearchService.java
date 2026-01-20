package com.exemplar.search;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.function.Factory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.reactor.ssl.TlsDetails;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.OpenSearchException;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.IndexSettings;
import org.opensearch.client.opensearch.indices.PutIndicesSettingsRequest;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;

import jakarta.annotation.PostConstruct;


//@Service
public class OpenSearchService {
	
	
	 OpenSearchClient openSearchClient;
	
	
	@PostConstruct
	public void init() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		   // System.setProperty("javax.net.ssl.trustStore", "/Users/venugopal/Documents/work/certs/opensearch.pem");
		   // System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

		    final HttpHost host = new HttpHost("https", "localhost", 9200);
		    final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		    // Only for demo purposes. Don't specify your credentials in code.
		    credentialsProvider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials("admin", "Ecompwd#24".toCharArray()));

		   /* final SSLContext sslcontext = SSLContextBuilder
		      .create()
		      .loadTrustMaterial(null, (chains, authType) -> true)
		      .build();
		      */
		    
		    final SSLContext sslcontext = SSLContextBuilder
				      .create()
				      .loadTrustMaterial(TrustAllStrategy.INSTANCE)
				      .build();
		    
		    
		 /*   CloseableHttpClient httpclient = HttpClients.custom()
		            .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
		                    .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
		                            .setSslContext(SSLContextBuilder.create()
		                                    .loadTrustMaterial(TrustAllStrategy.INSTANCE)
		                                    .build())
		                            .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
		                            .build())
		                    .build())
		            .build();
		    */
		  

		    final ApacheHttpClient5TransportBuilder builder = ApacheHttpClient5TransportBuilder.builder(host);
		    
		    
		    builder.setHttpClientConfigCallback(httpClientBuilder -> {
		      final TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
		        .setSslContext(sslcontext)
		        // See https://issues.apache.org/jira/browse/HTTPCLIENT-2219
		        .setTlsDetailsFactory(new Factory<SSLEngine, TlsDetails>() {
		          @Override
		          public TlsDetails create(final SSLEngine sslEngine) {
		            return new TlsDetails(sslEngine.getSession(), sslEngine.getApplicationProtocol());
		          }
		        })
		        .build();

		      final PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder
		        .create()
		        .setTlsStrategy(tlsStrategy)
		        .build();

		      return httpClientBuilder
		        .setDefaultCredentialsProvider(credentialsProvider)
		        .setConnectionManager(connectionManager);
		    });

		    final OpenSearchTransport transport = builder.build();
		    openSearchClient = new OpenSearchClient(transport);
	}
	
	
	
	public void createIndex() throws OpenSearchException, IOException {
		
		String index = "sample-index";
		CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index(index).build();
		openSearchClient.indices().create(createIndexRequest);

		IndexSettings indexSettings = new IndexSettings.Builder().autoExpandReplicas("0-all").build();
		
		
		PutIndicesSettingsRequest putIndicesSettingsRequest = new PutIndicesSettingsRequest.Builder().index(index).settings(indexSettings).build();
		openSearchClient.indices().putSettings(putIndicesSettingsRequest);
	}
	
	public void indexDocment() {
		String index = "sample-index";
		IndexData indexData = new IndexData("Ratan ", "Kumar");
		IndexRequest<IndexData> indexRequest = new IndexRequest.Builder<IndexData>().index(index).id("2").document(indexData).build();
		try {
			openSearchClient.index(indexRequest);
		} catch (OpenSearchException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
public void searchIndex() throws OpenSearchException, IOException {
	String index = "sample-index";
	SearchResponse<IndexData> searchResponse = openSearchClient.search(s -> s.index(index), IndexData.class);
	for (int i = 0; i< searchResponse.hits().hits().size(); i++) {
	  System.out.println(searchResponse.hits().hits().get(i).source());
	}

		
	}

}
