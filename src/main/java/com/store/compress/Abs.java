package com.store.compress;

public class Abs {
	 public void compress(String bitStr) {
		 this.buildModel(bitStr);
	 }
	 public byte[] encode(){
		return null;
	}
	
	public String decode(byte[] compressed){
		return null;
	}
	
	public void buildModel(String bitStr) {
		int ones = 0;
		int zeros = 0;
		for (int bit : bitStr.getBytes()) {
			if (bit == 48)
				zeros++;
			if (bit == 49)
				ones++;
		}
		int total = ones + zeros;
		System.out.print("Total:" + total + " 1's:" + ones + "	0's:" + zeros);

		double p1 = ((double) ones) / total;
		double p0 = 1 - p1;
		p1 = Double.parseDouble(String.format("%.2f", p1));
		p0 = Double.parseDouble(String.format("%.2f", p0));
		System.out.println(" p0=" + p0 + " , p1=" + p1);

		int[][] stable = new int[35][2];
		double newState = 0;

		for (int i = 1; i <= 14; i++) {
			//System.out.print("State : " + i + "  ");
			for (int j = 0; j < 2; j++) {
				if (j == 0) {
					newState = Math.floor(i / p0);
					//System.out.print(newState + " - " + p0 + " 	 ");
					stable[(int) newState][1] = 0;
				} else if (j == 1) {
					newState = Math.floor(i / p1);
					//System.out.print(newState + " -" + p1);
					stable[(int) newState][1] = 1;
				}
				stable[(int) newState][0] = i;
			}
			//System.out.println();
		}

		
		for (int i = 1; i < 25; i++) {
			System.out.print(i + "  ");
		 }
		System.out.println();
		for (int i = 1; i < 25; i++) {
			System.out.print(stable[i][0] + "  ");
			if(i>9 && stable[i][0]<10) {
				System.out.print(" ");
			}
		 }
		System.out.println();
		
		for (int i = 1; i < 25; i++) {
			System.out.print(stable[i][1] + "  ");
			if(i>9) {
				System.out.print(" ");
			}
		 }
	}
	
}
