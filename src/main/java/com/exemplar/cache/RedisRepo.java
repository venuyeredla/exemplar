package com.exemplar.cache;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepo extends CrudRepository<Student, String> {

}
