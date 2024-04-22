package com.interview.multithreaded;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class PubSubModel {
	
	
	 public void runPubSubModel() {
		 
		 LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<>();
		 
		 ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(2);
		 
		 Producer producer=new Producer(queue);
		 Consumer consumer=new Consumer(queue);
		 
		 newFixedThreadPool.submit(producer);
		 newFixedThreadPool.submit(consumer);
		 
		 newFixedThreadPool.shutdown();
		 
		 try {
			 newFixedThreadPool.awaitTermination(3000, TimeUnit.MILLISECONDS);
			 System.out.println("Shutting down?"+newFixedThreadPool.isShutdown()+ " Terminated : " + newFixedThreadPool.isTerminated() );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
		 
	 }
	
	
	class Producer implements Callable<String>{
		LinkedBlockingQueue<String> blockingDeque;

		public Producer(LinkedBlockingQueue<String> blockingDeque) {
			super();
			this.blockingDeque = blockingDeque;
		}

		@Override
		public String call() throws Exception {
			Stream.of("Venu","gopal","Reddy").forEach(s-> blockingDeque.add(s));
			return null;
		}
	}


	class Consumer implements Callable<String>{
		LinkedBlockingQueue<String> blockingDeque;

		public Consumer(LinkedBlockingQueue<String> blockingDeque) {
			super();
			this.blockingDeque = blockingDeque;
		}

		@Override
		public String call() throws Exception {
			
			Thread.sleep(1000);
			ArrayList<String> drainedList=new ArrayList<>();
			int drainTo = blockingDeque.drainTo(drainedList);
			System.out.println("Drain list :"+drainTo);
			drainedList.stream().forEach(System.out::println);
			return null;
		}
	}



}
