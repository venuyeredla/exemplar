package com.interview.multithreaded;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementing Runnable or extending Thread.
 * 
 * Mutex - synchronized and locks
 * Semaphores - For limiting numbers threads to access a resource.
 * 
 * Monitors (Synchronized) vs Locks (through locks package)
 * 
 * Semaphores
 * @author venugopal
 *
 */
public class MutexingThreads {
	
	private static final Logger LOG=LoggerFactory.getLogger(MutexingThreads.class);
	
	public void synchronizeThreads() {
		
		Mutexer mutexer=new Mutexer();
		
		mutexer.setFlag(true);
		
		Runnable runnable1= ()-> {
			 IntStream.of(1,3,5,7).forEach(a ->{
				    if(mutexer.acquireEven()) {
				    	 System.out.println(a+", ");
				    //	 LOG.info("Thread 1 :{}",mutexer.flag);
				    	 mutexer.unlockEven();
				     }
					
			  }
			);
		};
		
		Runnable runnable2= ()-> {
			 IntStream.of(2,4,6,8).forEach(a -> {
				// LOG.info("Thread 2 ={}",mutexer.flag);
				 if(!mutexer.acquireOdd()) {
					 System.out.println(a+", ");
					mutexer.unlockOdd();
				 }
				
			 }
			
			);
		};
		
		Thread t1=new Thread(runnable1);
		t1.start();
		sleep(10);
		new Thread(runnable2).start();
	}
	
	public void sleep(int time) {
		 try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}
	
	
	class Mutexer{
		private boolean flag;
		
		public void setFlag(boolean flag) {
			this.flag=flag;
		}
		public synchronized boolean acquireEven() {
			while(!flag) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return flag;
		}
		
		public synchronized void unlockEven() {
			this.flag=false;
			notify();
		}
		
		
		public synchronized void unlockOdd() {
			this.flag=true;
			notify();
		}
		
		
		public synchronized boolean acquireOdd() {
			while(flag) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return flag;
		}
		
		public void sleep(int time) {
			 try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
		}

	}
	
	  public void futureTasks() {
		  
			Callable<String> callable=()->{
				
				return null;
				
			};
			
			FutureTask<String> futureTask=new FutureTask<>(callable);
			
			ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
			newSingleThreadExecutor.execute(futureTask);
			
			while(!futureTask.isDone()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			newSingleThreadExecutor.shutdown();
		  
	  }
	
	
	
	public void testLocks() {
		
	}

}



 class Lock{

	  private boolean isLocked = false;

	  public synchronized void lock()
	  throws InterruptedException{
	    while(isLocked){
	      wait();
	    }
	    isLocked = true;
	  }

	  public synchronized void unlock(){
	    isLocked = false;
	    notify();
	  }
	}


