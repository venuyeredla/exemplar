package com.store.rdbms;

import com.store.io.ByteBuf;

public interface Store {
	
	public boolean writeSchemaInfo(Schema schemaInfo,int pageNum);
	public Schema getSchemaInfo(int pageNum) ;
	public boolean writeTable(Table table,int pageNum);
	public Table getTable(int pageNum);
	public boolean writeTableRow();
	
	public void writeBlock(int blockNum, ByteBuf block);
	public ByteBuf readBlock(int blockNum);
	public void writeIdxNode(BtreeNode node);
	public BtreeNode readIdxNode(int nodeId);
	public void addToBuffer(int key,BtreeNode node);
	public String getPageList();
	public boolean isExisted();

}
