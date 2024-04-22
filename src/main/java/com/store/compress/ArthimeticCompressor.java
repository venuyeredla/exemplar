package com.store.compress;

public class ArthimeticCompressor implements Compressor{
	private byte bits=8;
	int MASK=(1<<bits)-1;           //Max range number with all ones. 1111...111
	int TOP_MASK=1<<bits-1;         //Top bit mask. 10000...0000
	int SECOND_MASK=1<<bits-2;      //Second bit mask. 01000...000
	int MAX_ALLOWED=SECOND_MASK-1;  // 00111..111

	private int underflow=0;
	BitStream bitStream=new BitStream();
	private FreqTable freqTable;
	
	private int low,high;
	
	public ArthimeticCompressor() {
		//TODO Auto-generated constructor stub
	}

	public void init() {
		System.out.println("MASK:"+MASK+"-"+decToBin(MASK));
		System.out.println(" TOP_MASK:"+TOP_MASK+"-"+decToBin(TOP_MASK));
		System.out.println(" SECOND_MASK:"+SECOND_MASK+"-"+decToBin(SECOND_MASK));
	}
	public void initcompress() {
	}
	
	@Override
	public byte[] compress(byte[] bytes) {
		//this.init();
		System.out.println("Data size: "+bytes.length);
		this.low=0; this.high=(1<<8)-1;
		this.freqTable=new FreqTable(MAX_ALLOWED,256);
		this.freqTable.buildRanges(bytes);
		for(int i=0;i<bytes.length;i++) {
			this.applySymbolRange(bytes[i]);
			while(((low ^ high) & TOP_MASK)==0) {  //When msb bit matches it is written to stream.
				 eshift(low);
		         low=(low<<1) & MASK;
		         high=((high<<1) & MASK) | 1;
			}
			// While the second highest bit of low is 1 and the second highest bit of high is 0
			while ((low & ~high & SECOND_MASK) != 0) {
				eunderflow();
				low = (low << 1) & (MASK >>> 1);
				high = ((high << 1) & (MASK >>> 1)) | TOP_MASK | 1;
			}
			
		}
		for(int a=0;a<10;a++) {
		   bitStream.writeBit(0);
		}
		bitStream.flushBits();
		return bitStream.getBytes();
	}
	
	private int code=0;
	
	@Override
	public byte[] decompress(byte[] bytes) {
		int size=132;
		this.bitStream=new BitStream(bytes);
		BitStream outputBytes=new BitStream();
		this.low=0;this.high=MASK;
		int total=this.freqTable.getTotal();
		//initialize the code
		for(int i=0;i<bits;i++) {
			code=code<<1 | bitStream.readBit();
		}
		try {
		// Decoding the code;
		for(int j=0;j<size;j++) {
			if(j==129) {
				System.out.println("Debug point at" + j);
			}
			int range=high-low+1;
			int value=((code-low+1) * total-1)/range;
			int symb=freqTable.getSymbol(value);
			outputBytes.writeByte(symb);
			//System.out.print((char)symb);
			this.applySymbolRange(symb);
			if(j==size-1) {
				return outputBytes.getBytes();
			}
			
			while (((low ^ high) & TOP_MASK) == 0) {
				dshift();
				low = (low << 1) & MASK;
				high = ((high << 1) & MASK) | 1;
			}
			// While the second highest bit of low is 1 and the second highest bit of high is 0
			while ((low & ~high & SECOND_MASK) != 0) {
				dunderflow();
				low = (low << 1) & (MASK >>> 1);
				high = ((high << 1) & (MASK >>> 1)) | TOP_MASK | 1;
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputBytes.getBytes();
	}
	
	public void applySymbolRange(int symbol) {
		int symLow=this.freqTable.getLow(symbol), symHigh=this.freqTable.getHigh(symbol);
		int cumulative=this.freqTable.getTotal();
		int range=high-low+1; //Adding 1 to include upper bound also.
		high=low+(symHigh*range)/cumulative-1; // subtracting 1 to avoid overlap of intervals
		low=low+(symLow*range)/cumulative;
	}
	
	public void eshift(int num) {
		int bit=num>>(bits-1)&1;
		bitStream.writeBit(bit);
		for (; underflow > 0; underflow--) {
			bitStream.writeBit(bit ^ 1);
		}
	}
	
	public void eunderflow() {
		underflow++;
	 }
	
	public void dshift() {
		code = ((code << 1) & MASK) |bitStream.readBit();
	}
	
	public void dunderflow() {
		code = (code & TOP_MASK) | ((code << 1) & (MASK >>> 1)) | bitStream.readBit();
	}
	
	
	public static String decToBin(int num) {
		if(num==0) return "0";
		String bitStr="";
		while(num>0) {
			bitStr=(num&1)+bitStr; //Extracting last  bit
			num=num>>1;
		}
		return bitStr;
	}
	
	public static int binToDec(String binary) {
		int num=0;
		for(int i=0;i<binary.length();i++) {    // 100 => 4   
			int bit=binary.charAt(i)=='0'?0:1;
			num =num<<1 | bit;  // Adding binary number to a number
		}
		return num;
	}
}

class FreqTable{
	private int[] frequencies;
	private int[] cumulatives;
	private int total;
	private int MAX_PROBABLITY;
	public FreqTable(int max,int size) {
		frequencies=new int[size];
		cumulatives=new int[size];
		this.MAX_PROBABLITY=max;
	 }
	
	 public void buildRanges(byte[] data) {
		for(int i=0;i<data.length;i++) {
			frequencies[data[i]]++;
		}
		this.total=data.length;
		//if the cumulative frequncies > bits-2 we should rescale it.
		if(this.total>=MAX_PROBABLITY)
		{
		  int rescaleValue=(this.total/MAX_PROBABLITY)+1;
		  for(int j=0;j<frequencies.length;j++) {
			  if(frequencies[j]>rescaleValue) {
				  frequencies[j]=frequencies[j]/rescaleValue;
			  }else if(frequencies[j] !=0){
				  frequencies[j]=1;
			  }
		  }
		}
		cumulatives[0]=frequencies[0];
		int cumulative=0;
		for(int j=1;j<frequencies.length;j++) {
				cumulatives[j]=cumulative+frequencies[j];
				cumulative=cumulatives[j];	
		}
        this.total=cumulative;
        System.out.println("Symbol ranges with upper bounds");
		for(int k=1;k<frequencies.length;k++) {
			if(frequencies[k]!=0) {
			  System.out.print((char)k+"=["+cumulatives[k-1]+","+cumulatives[k]+"],");
			}
		}
		System.out.println("");
	}
	
	public int getLow(int symbol) {
		if(symbol==0){
			return 0;
		}
		return cumulatives[symbol-1];	
	}
	public int getHigh(int symbol) {
		return cumulatives[symbol];
	}
	
	public int getSymbol(int probability) {
		int temp=0;
         for(int i=0;i<256;i++) {
        	 if(probability<this.cumulatives[i]) {
        		 temp=i;
        		 break;
        	 }
         }
		return temp;
	}
	
	public int getTotal() {
		return this.total; 
	}
}
