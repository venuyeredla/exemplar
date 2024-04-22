package com.store.ds;

public interface ITree<T> {
	public boolean search(T key);
	public boolean insert(T key);
	public boolean delete(T key);
}
