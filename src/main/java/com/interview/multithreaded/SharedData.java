package com.interview.multithreaded;


public class SharedData{
	private String msg;
	Lock  lock=new Lock();
	

	public SharedData(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void Updatemsg(String newMsg) {
		try {
			lock.lock();
			this.msg=newMsg;
			lock.unlock();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}