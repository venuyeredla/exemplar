package com.exemplar.config;

import javax.sql.DataSource;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
//import org.springframework.integration.jdbc.metadata.JdbcMetadataStore;
//import org.springframework.integration.metadata.MetadataStore;

@SpringBootConfiguration
@ComponentScan(basePackages = "com.exemplar")
public class AppConfig {
	
	@Bean
	public DefaultLockRepository DefaultLockRepository(DataSource dataSource){
	   return new DefaultLockRepository(dataSource);
	}

	@Bean
	public JdbcLockRegistry jdbcLockRegistry(LockRepository lockRepository){
	   return new JdbcLockRegistry(lockRepository);
	}
	
/*	@Bean
	public MetadataStore metadataStore(DataSource dataSource) {
		JdbcMetadataStore jdbcMetadataStore = new JdbcMetadataStore(dataSource);
		jdbcMetadataStore.setTablePrefix("SPRING_INT");
	    return jdbcMetadataStore;
	}
	*/
}
