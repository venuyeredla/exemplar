package com.store.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

 /**
 * Wraps byte array and default size is 512 bytes. and it grows as per need.
 * 
 */
public class ByteBuf{
	int capacity;
	private byte[] bytes;
	private int wPos;//points to the next position to be written.
	private int rPos;

	public ByteBuf() {
		this.capacity=StoreConstants.BUFFER_SIZE;
		bytes=new byte[this.capacity];
		wPos=0;
		rPos=-1;
	}
	public ByteBuf(int size) {
		capacity=size;
		bytes=new byte[capacity];
		wPos=0;
		rPos=-1;
	}
	public ByteBuf(byte[] b) {
		capacity=b.length+10;
		bytes=new byte[capacity];
		for(int i=0;i<b.length;i++) 
			bytes[wPos++]=b[i];
		wPos=b.length;
		rPos=-1;
	  }
	
	private void resize() {
		capacity=capacity+100;
		bytes=Arrays.copyOf(bytes, capacity);
		
	}
	
	
	 private ByteBuf writeByte(byte b) {
		 if(wPos==capacity) {
			 resize();
			 System.out.println("Block is full: Block size:"+bytes.length+ " Position to write : "+wPos+1);
		 }
		bytes[wPos++]=b;
		return this;
	 }
	/**
	 * Writes lower order 8 bits;
	 * @param b
	 * @return
	 */
	public ByteBuf writeByte(int b) {
		this.writeByte((byte)b);
		return this;
	  }
	 /**
	 * returns signed byte.
	 * @return
	 */
	public byte readByte() {
		 try {
		   if(++rPos>=wPos) throw new OutOfRangException("There are no bytes at postion:"+rPos);
			 return bytes[rPos];
		} catch (OutOfRangException e) {
			e.printStackTrace();
			System.exit(1);
		  }
       return -1;
	   }

	/**
	 * Reads one byte and returns as short. other wise last bit considered as negative number.
	 * @return
	 */
	public short read() {
		 try {
		   if(++rPos>=wPos) throw new OutOfRangException("Read position is greater than size of buffer. Buffer size: "+(wPos-1) +" Read postion ="+rPos);{
			   byte b=bytes[rPos];
			   return (b&0x80)==0x80?(short)(b & 0xff):b;
		   }
		 } catch (OutOfRangException e) {
			e.printStackTrace();
			System.exit(1);
		  }
        return -1;
	   }
	
	public ByteBuf write(short s) {
		writeByte(s >> 8); 
		writeByte(s);
		return this;
	}
	public short readShort() {
		return  (short)((read() << 8)|(read()));
	 }
	
	public ByteBuf write(int i) {
		writeByte(i >> 24);	
		writeByte(i >> 16);
		writeByte(i >> 8);
		writeByte(i);
		return this;
	 }
	public int readInt() {
		return (read() << 24) | (read() << 16) |(read() << 8)|(read());
	 }
	public ByteBuf writeVInt(int i) {
		while((i & ~0x7F) !=0 ) {
			writeByte(((i&0x7F) | 0x80));
			i>>=7;
		}
		writeByte(i);
		return this;
	}
	
	public int readVInt() {
		int b=readByte();
		if(b>0) return b;  // it means like it is less than < 127 and it is one bit integer. i.e last bit is used for sign in byte
		int i=b & 0x7F;
		b=readByte();
		i=i | (b & 0x7F)<<7;
	    if(b>0) return i;
	    b=readByte();
		i=i | (b & 0x7F)<<14;
		if(b>0) return i;
		 b=readByte();
		i=i | (b & 0x7F)<<21;  // 28 bits were read. 4 bits need to be read to yet
		if(b>0) return i;
		b=readByte();
		 i=i | (b & 0x0F)<<28;
		if(b>0) return i;
		if ((b & 0xF0) == 0) return i;
		return 0;
	}
	
	public ByteBuf writeLong(long l) {
		writeByte((byte)l >> 56);	writeByte((byte)(l >> 48));	writeByte((byte)(l >> 40)); writeByte((byte)(l >> 32));
		writeByte((byte)(l >> 24));	writeByte((byte)(l >> 16));	writeByte((byte)(l >> 8)); writeByte((byte)(l));
		return this;
	  }
	public long readLong() {
		 return (read() << 56) | (read() << 48) |(read() << 40)|(read() << 32)|
				(read() << 24) | (read() << 16) |(read() << 8)|(read());
	}
	
	public ByteBuf write(byte[] b) {
		for(int i=0;i<b.length;i++) {
			bytes[wPos++]=b[i];
		}
		return this;
	  }
	
		/**
	 * Adds the string bytes length as Vint and string bytes
	 * @param str
	 */
	public ByteBuf write(String str) {
		writeVInt(str.getBytes().length);
		this.write(str.getBytes());
		return this;
	}
	
	public String readString() {
		 return new String(readBytes(readVInt()));
	}
	
	public ByteBuf write(boolean b) {
		if(b) writeByte(1); else writeByte(0);
		return this;
	}
	
	public boolean readBoolean() {
		  byte b=this.readByte();
		  return b==1?true:false;
	}
	
	public ByteBuf write(LinkedHashMap<String,String> map) {
		writeVInt(map.keySet().size());
		map.forEach((key,value)-> {	write(key);	write(value);});
		return this;
	}
	public LinkedHashMap<String,String> readMap(){
		LinkedHashMap<String,String> map=new LinkedHashMap<>();
		for (int i = 0; i < readVInt(); i++) {
		   map.put(readString(), readString());
		}
		return map;
	}
	public List<String> readList() {
		List<String> list=new ArrayList<>();
		for(int i=0;i<readInt();i++) {
			list.add(readString());
		}
		return list;
	}
	public ByteBuf writeList(List<String> strings) {
		write(strings.size());
		strings.forEach(str -> write(str));
		return this;
	}
	
	
	public byte[] getBytes() {
		return Arrays.copyOf(bytes, wPos);
	}
	
	 /**
	  * Don't increase the read position.
	  * @param from
	  * @param to
	  * @return
	  */
     public byte[] readBytes(int from ,int to) {
    	return Arrays.copyOfRange(bytes, from, to);
	 }
   
    /**
     * Return all bytes from current position to specified position.
     * @param len
     * @return
     */
     public byte[] readBytes(int size) {
 		 byte[] b=new byte[size];
 		 for (int i = 0; i < size; i++) b[i]=readByte();
 		 return b;
 	}
	
	public int size() {
		return wPos;
	}

	/**
	 * Useful to set byte at given position.
	 * @param index
	 * @param b
	 */
	public void setByte(int index,byte b) {
		bytes[index]=b;
	}
	
	public void setReadPos(int pos) {
		  rPos=pos;
	}
    public int readPos() {
    	return rPos;
    }
 
    public void resetByteRead() {
    	this.rPos=-1;
    }
	@Override
	public String toString() {
		return "ByteBuf [wPos=" + wPos + ", rPos=" + rPos + "]";
	}
}

class OutOfRangException extends Exception{
	private static final long serialVersionUID = 1L;
	public OutOfRangException(String message) {
		super(message);
	}
}