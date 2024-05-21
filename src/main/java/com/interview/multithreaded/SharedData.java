package com.interview.multithreaded;

import java.util.concurrent.locks.ReentrantLock;

public class SharedData{
	private String msg;
	
	ReentrantLock  lock=new ReentrantLock();
	

	public SharedData(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getMsg() {
		
		String response=null;
		lock.lock();
		response=msg;
		lock.unlock();
		
		return response;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public /*synchronized */ void Updatemsg(String newMsg) {
		lock.lock();
		this.msg=newMsg;
		lock.unlock();
		
	}
	
	
	
}