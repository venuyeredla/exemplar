package com.store.kvstore;

import java.util.List;
import java.util.Optional;

public interface KVStore {
	
	public boolean put(String key, String value);
	public Optional<String> get(String key);
	public List<String> getAll();
	public List<String> getBetween();

}
