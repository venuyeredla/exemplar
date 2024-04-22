package com.exemplar.multithread;

import org.junit.jupiter.api.Test;

import com.interview.multithreaded.MutexingThreads;
import com.interview.multithreaded.PubSubModel;


public class MultiThreadTest {
	
	@Test
	public void testThread() {
		MutexingThreads multiThreading = new MutexingThreads();
		multiThreading.synchronizeThreads();
	}

	@Test
	public void pubsubModel() {
		PubSubModel pubsubModel=new PubSubModel();
		pubsubModel.runPubSubModel();
	}

}
