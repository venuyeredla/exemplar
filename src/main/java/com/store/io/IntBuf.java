package com.store.io;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntBuf {
	private static final Logger LOG=LoggerFactory.getLogger(IntBuf.class);
	private int[] ints;
	private int wPos;
	private int rPos;
	private int initCapacity=512;
	private int capacity;

	public IntBuf() {
		capacity=initCapacity;
		ints=new int[capacity];
		wPos=0;//points to the next position to be inserted.
		rPos=-1;
	}
	
     public void grow() {
    		 capacity=capacity+512;
        	 int old=this.ints.length;
        	 //LOG.debug("Size will be increased");
        	 ints=Arrays.copyOf(ints, capacity);
        	 LOG.debug("byte array resized from - to :"+old+" - "+this.ints.length);
    	 }
     
     public IntBuf write(int num) {
 		if(wPos+1>=capacity) this.grow();
 		ints[wPos++]=num;
 		return this;
 	 }
     
     public int read() {
		 try {
		   if(++rPos>=wPos) throw new OutOfRangException("There are no bytes at postion:"+rPos);
			 return ints[rPos];
		} catch (OutOfRangException e) {
			e.printStackTrace();
			System.exit(1);
		  }
         return -1;
	   }
     
     
     public int size() {
 		return wPos;
 	   }
     
 	public int[] getInts() {
 		return Arrays.copyOf(ints, wPos);
	}
     }
	
	
	
