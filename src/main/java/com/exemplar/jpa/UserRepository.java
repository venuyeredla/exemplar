package com.exemplar.jpa;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.exemplar.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
	
	//@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	//@Query("SELECT c FROM Customer c WHERE c.orgId = ?1")
	Optional<User> findByEmail(String email);


}
