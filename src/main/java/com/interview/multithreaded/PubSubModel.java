package com.interview.multithreaded;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class PubSubModel {
	
	
	 public void runPubSubModel() {
		 
		 LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<>();
		 
		 ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(2);
		 
		 //Runnable
		 // newFixedThreadPool.submit(() -> {}); 
		 
		 Producer producer=new Producer(queue);
		 Consumer consumer=new Consumer(queue);
		 
		 newFixedThreadPool.submit(producer);
		 newFixedThreadPool.submit(consumer);
		 
		 newFixedThreadPool.shutdown();
		 //newFixedThreadPool.awaitTermination(100, TimeUnit.MILLISECONDS);
		 
		 try {
			 newFixedThreadPool.awaitTermination(3000, TimeUnit.MILLISECONDS);
			 System.out.println("Shutting down?"+newFixedThreadPool.isShutdown()+ " Terminated : " + newFixedThreadPool.isTerminated() );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
		 
	 }
	 
	 
	 public void testCompletableFuture() throws InterruptedException, ExecutionException {
		 
		 CompletableFuture<String> completeFuture1 = CompletableFuture.supplyAsync(() -> {
			 
			 
			 return "Hello";
		 });
		 
		 CompletableFuture<String> completeFuture2 = completeFuture1.thenApply(data -> {
			 
			 return data +" World";
		 });
		 
		 // completeFuture2.get();
		 
		 
		 CompletableFuture<String> completeFuture23= completeFuture1.thenApply(data -> {
			 
			 return "From 3rd pool";
		 });
		 
		 CompletableFuture<Void> allOf = CompletableFuture.allOf(completeFuture2,completeFuture23);
		 
		 allOf.join();
	
		 
		 String string = completeFuture2.get();
		 
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
			
			Stream.of("I","am","working").forEach(s-> blockingDeque.add(s));
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


  
