package com.store.compress;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.store.compress.IntegerCompressor.INT_MODE;
import com.store.io.ByteBuf;

/**
 * Iterates all byte content and counts symbol frequencies. 
 * @author vyeredla
 *
 */
public class StaticModel {
	  private static final Logger LOG = LoggerFactory.getLogger(StaticModel.class);
	  int[] freqs=new int[256];
	  SymDict[] symDict;
	  public StaticModel() {}
	  
	  public void calFreq(byte[] bytes) {
		    for (byte c : bytes) freqs[c]++;
		    int count=0;
		    for(int f:freqs) {
		    	if(f!=0) {
		    		count++;
		    	 }
		    }
		    int j=0;
		    symDict=new SymDict[count];
		    for(int i=0;i<freqs.length;i++) {
		    	if(freqs[i]!=0) {
		    		symDict[j++]=new SymDict(i, freqs[i]);
		    		//System.out.print(i+"-"+freqs[i]+" , ");
		    	}
		    }
	  }
	  public int[] writeFreqs(ByteBuf byteBuf) {
		  int keys[]=new int[symDict.length];
		    for(int i=0;i<symDict.length;i++) {
		    	 keys[i]=symDict[i].key;
		     }
		   IntegerCompressor integerCompressor=new IntegerCompressor();
		   byte[] keysCompressed=integerCompressor.compress(keys, INT_MODE.DELTA_BITPACKING);
		   byteBuf.writeByte(0);
		   byteBuf.write(keysCompressed);
		   int valsPointer=byteBuf.size();
		   byteBuf.setByte(0, (byte)valsPointer);
		   LOG.debug("Size of byteBuf :"+valsPointer);
		   for (SymDict kv : symDict) {
				  byteBuf.writeVInt(kv.val);
			}
		   LOG.info("Freqs size: "+byteBuf.size());
		   return keys;
	   }
	  
	  public int[] readFreqs(ByteBuf byteBuf) {
		  IntegerCompressor integerCompressor=new IntegerCompressor();
		  int valsPointer=byteBuf.readByte();
		  byte[] bytes=byteBuf.getBytes();
		  int[] keys=integerCompressor.decompress(Arrays.copyOfRange(bytes, 1, valsPointer),INT_MODE.DELTA_BITPACKING);
		  byteBuf.setReadPos(valsPointer-1);
		  symDict=new SymDict[keys.length];
		  for(int i=0;i<keys.length;i++) {
			  SymDict keyVal=new SymDict(keys[i], byteBuf.readVInt()); 
			  symDict[i]=keyVal;
			  System.out.print(keyVal.key+"-"+keyVal.val+" , ");
		  }
		  return keys;
	  }
	  
	  
	  public SymDict[] getFreqs(){
		  return symDict;
	  }
	  
}

/**
 * Holds sorted symbols and frequencies.
 * @author vyeredla
 *
 */
class SymDict{
   	int key;
   	int val;
	public SymDict(int key, int val) {
		super();
		this.key = key;
		this.val = val;
	}
   }

  
