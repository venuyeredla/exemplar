package com.exemplar.config;

import javax.sql.DataSource;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.jdbc.metadata.JdbcMetadataStore;
import org.springframework.integration.metadata.MetadataStore;

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
	
	@Bean
	public MetadataStore metadataStore(DataSource dataSource) {
		JdbcMetadataStore jdbcMetadataStore = new JdbcMetadataStore(dataSource);
		jdbcMetadataStore.setTablePrefix("SPRING_INT");
	    return jdbcMetadataStore;
	}

	@Bean
	@Primary
	//@ConfigurationProperties(prefix="spring.datasource")
	public DataSource primaryDataSource(DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	    //return DataSourceBuilder.create().build();
	}
	
	//@Bean
	//@ConfigurationProperties(prefix="spring.secondDatasource")
	public DataSource secondaryDataSource() {
	    return DataSourceBuilder.create().build();
	}
	/* jdbc:mysql:loadbalance://slave1:3306,slave2:3306/database_name
	 
	 
	 jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=tcp)(HOST=RAC1)(PORT=1521))(ADDRESS=(PROTOCOL=tcp)(HOST=RAC2)(PORT=1521))...) (CONNECT_DATA=(SERVICE_NAME=myServiceName)))
	 
	 
	 */
	
	@Bean
	public IntegrationDataSourceScriptDatabaseInitializer customIntegrationDataSourceInitializer(DataSource dataSource) {
	    return new IntegrationDataSourceScriptDatabaseInitializer(dataSource, new DatabaseInitializationSettings());
	}

}
