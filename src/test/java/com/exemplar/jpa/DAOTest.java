package com.exemplar.jpa;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.exemplar.entity.User;


@SpringBootTest(classes = JPATestConfig.class)
@ActiveProfiles("dev")
public class DAOTest {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void testDBConnection() {
		try {
			if (Objects.nonNull(dataSource.getConnection())) {
				System.out.println("Connected susessfully");
				
			}else {
				System.out.println("Connected failed");
			}
		Optional<User> userOptional=userRepository.findByEmail("venugopal@ecom.com");
		if (userOptional.isEmpty()) {
			
		}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
