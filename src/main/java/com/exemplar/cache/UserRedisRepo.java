package com.exemplar.cache;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.exemplar.entity.RedisUser;

@Repository
public interface UserRedisRepo extends CrudRepository<RedisUser, String> {

}
