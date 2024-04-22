package com.store.compress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.store.io.ByteBuf;

/**
 * Wrapper over ByteBuf
 * @author vyeredla
 *
 */
public class BitStream extends ByteBuf {
	private static final Logger LOG=LoggerFactory.getLogger(BitStream.class);
	private int bitBuf;
	private short widx;
	private short ridx;
	public BitStream() {}
	
	public BitStream(byte[] b) {
		 super(b);
	}
	public void writeBit(int bit) {
    	int actualBit=bit &1;
    	this.bitBuf=this.bitBuf<<1 | actualBit;
    	this.widx +=1;
    	if(this.widx==8) {
    		this.writeByte(this.bitBuf);
    		this.bitBuf=0;
    		this.widx=0;
    	}
    }
    
    /**
     * Flushes unwritten bits
     */
    public void flushBits() {
    	int toBe=8-this.widx;
    	for(int i=0;i<toBe;i++) {
    		this.writeBit(0);
    	 }
    }
    public int readBit() {
    	if(this.ridx==0) {
    		this.bitBuf=this.readByte();
    		this.ridx=8;
    	}
    	int bit=(this.bitBuf >> (this.ridx-1)) & 1;
    	ridx -=1;
    	return bit;
    }
}
