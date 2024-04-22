package com.store.compress;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.store.io.ByteBuf;

public class HuffManCoding implements Compressor {
	private HuffManNode[] nodes;
	private int heapSize;
	private LinkedHashMap<Integer, String> hCodes=new LinkedHashMap<Integer, String>();
	public HuffManCoding() { }
	
	@Override
	public byte[] compress(byte[] data) {
		StaticModel model = new StaticModel();
		model.calFreq(data);
		ByteBuf byteBuf = new ByteBuf();
		model.writeFreqs(byteBuf);
		this.buildHCodes(model.getFreqs());
		BitStream bitStream = new BitStream(byteBuf.getBytes());
		bitStream.writeVInt(data.length);
	    for (int i = 0; i < data.length; i++) {
	    	int dat=data[i];
			String code = hCodes.get(dat);
			while(code.length()>0) {
				int bit=Integer.parseInt(code.substring(0, 1));
				bitStream.writeBit(bit);
				code=code.substring(1);
			}
		 }
	    bitStream.flushBits();
		System.out.println("\n Actual -> Compressed  :: "+ data.length+ " ->  "+bitStream.getBytes().length);
		return bitStream.getBytes();
	}
	
	
	public void buildHCodes(SymDict[] dict) {
		 int capacity=dict.length;
		 this.nodes=new HuffManNode[capacity];
		 this.heapSize=0;
		 for(SymDict sd: dict) {
			 HuffManNode node=new HuffManNode(sd.key,sd.val);
			 this.add(node);
		 }
	   this.buildHuffManTree();
	   this.printCodes(this.nodes[0], "");
	   System.out.print("Huffmann codes: ");
	   hCodes.forEach((key,val)->{
		   char ch=(char)(int)key;
		   //System.out.print(key+ "("+ch+")-"+val+" , ");
		   System.out.print(ch+"-"+val+",");
	   });
  }
	
	@Override
	public byte[] decompress(byte[] compressed) {
		BitStream byteBuf=new BitStream(compressed);
		StaticModel staticModel=new StaticModel();
		staticModel.readFreqs(byteBuf);
		this.buildHCodes(staticModel.getFreqs());
		Map<String,Integer> dhCodes=new HashMap<>();
		hCodes.forEach((key,val)->{ dhCodes.put(val, key); });
		int datasize=byteBuf.readVInt();
		byte[] data=new byte[datasize];
		String code="";
		for(int i=0;i<datasize;i++) {
			while(!dhCodes.containsKey(code)) {
				code=code+byteBuf.readBit();
			}
			int num=dhCodes.get(code);
			data[i]=(byte)num;
			code="";
		}
		return data;
	}
	
	private void buildHuffManTree() {
		while(this.heapSize>1) {
			HuffManNode first=this.extractMin();
			HuffManNode second=this.extractMin();
			HuffManNode node=new HuffManNode(first.freq+second.freq);
			node.left=first;
			node.right=second;
			this.add(node);;
		}
	}
	
	private void printCodes(HuffManNode node, String str) {
		if(node!=null) {
			if(node.left!=null) {
				this.printCodes(node.left, str+"0");
			}
			if(node.right!=null) {
				this.printCodes(node.right, str+"1");
		    }
			if(node.left==null && node.right==null)
				hCodes.put(node.key, str);
		}
	}
	
	public void add(HuffManNode node) {
		int i=heapSize++;
		this.nodes[i]=node;
		while(i!=0 && validateHeapfy(parentIndex(i),i)) {
			this.swap(i, parentIndex(i));
			i=parentIndex(i);
		 }
	  }
	
	public void printHeap() {
		for(int i=0;i<heapSize;i++) {
			HuffManNode node=this.nodes[i];
			System.out.print(node.key+"--"+node.freq+" , ");
		}
		System.out.println();
	}
	
	public HuffManNode extractMin() {
		   HuffManNode temp=this.nodes[0];
		   this.nodes[0]=this.nodes[--heapSize];
		   this.nodes[heapSize]=null;
		   this.minHeafFy(0);
		   return temp;
	}
	
	private boolean validateHeapfy(int j ,int i) {
		HuffManNode parent=this.getNodeAt(j);
		HuffManNode child=this.getNodeAt(i);
		return parent.freq>child.freq;
	}
	// Heafies from top to bottom.
	private void minHeafFy(int i) {
		int l=2*i+1;
		int r=2*i+2;
		int smallest=i;
		if(l<heapSize && validateHeapfy(smallest,l)) smallest=l;
		if(r<heapSize && validateHeapfy(smallest,r)) smallest=r;
		if(smallest!=i) {
				swap(i, smallest);
				this.minHeafFy(smallest);
		 }
	 }
	
	public int getFreq(int i) {
		return this.getNodeAt(i).freq;
	}
	
	private void swap(int first ,int second) {
		HuffManNode temp=this.nodes[first];
		this.nodes[first]=this.nodes[second];
		this.nodes[second]=temp;
	}

	private int parentIndex(int i) {
		return (i-1)/2;
	}
	
	private HuffManNode getNodeAt(int i) {
		return this.nodes[i];
	}

  }

class HuffManNode{
	protected int freq;
	protected int key;
	protected HuffManNode left, right;
	public HuffManNode(int freq) {
		this.freq = freq;
	}
	public HuffManNode(int key,int freq) {
		this.freq = freq;
		this.key = key;
	}
	@Override
	public String toString() {
		return "HuffManNode [freq=" + freq + ", key=" + key + "]";
	}
}