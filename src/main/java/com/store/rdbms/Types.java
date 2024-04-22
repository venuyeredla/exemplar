package com.store.rdbms;

public enum Types {
	BYTE(0,1),SHORT(1,2),INT(2,4),LONG(3,8),STRING(4,64);
	/**
	 * 
	 * @param num --used for storage;
	 */
	Types(int num, int noOfBytes) {
		this.val=(byte)num;
		this.noOfBytes=(short)noOfBytes;
	}
	private byte val;
	private short noOfBytes;
	
	public byte typeNum() {
		return this.val;
	}
	public short byteSize() {
		return this.noOfBytes;
	}
	
	public static Types getType(int num){
		Types type = null;
		switch (num) {
		case 0:
			type=Types.BYTE;
			break;
		case 1:
			type=Types.SHORT;
			break;
		case 2:
			type=Types.INT;
			break;
		case 3:
			type=Types.LONG;
			break;
		case 4:
			type=Types.STRING;
			break;
		default:
			break;
		}
		return type;
	}
 }
