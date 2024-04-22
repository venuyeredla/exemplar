package com.store.rdbms;

public enum BtreeNodeType {
	ROOT(1),INTERNAL(2),LEAF(3);
	 
	private int nodeVal;
	BtreeNodeType(int nodeVal) {
		this.nodeVal=nodeVal;
	}
	public byte getNodVal() {
		 return (byte)this.nodeVal;
	}

	public static BtreeNodeType getNodeType(int num){
		BtreeNodeType type = null;
		switch (num) {
		case 1:
			type=BtreeNodeType.ROOT;
			break;
		case 2:
			type=BtreeNodeType.INTERNAL;
			break;
		case 3:
			type=BtreeNodeType.LEAF;
			break;
		default:
			break;
		}
		return type;
	}
	
}
