package com.store.rdbms;

import static com.store.io.StoreConstants.BLOCK_SIZE;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.store.io.ByteBuf;
import com.store.io.DataReader;
import com.store.io.DataWriter;
import com.store.io.StoreConstants;

public class StoreToFile implements Store,Closeable{
	
	private static final Logger LOG = LoggerFactory.getLogger(StoreToFile.class);
	
	private static String DB_PATH="/Users/venugopal/Documents/work/store/rdbms/dbname.db";
	
	private DataWriter writer;
	private DataReader reader;
	Map<Integer,BtreeNode> bufferPages=null;
	private boolean existed;

	public StoreToFile(String dbName,String userName, String passWord, boolean ifNot) {
		dbName=dbName.toLowerCase();
		DB_PATH=DB_PATH.replaceAll("dbname", dbName);
		LOG.info("OS block size is : "+BLOCK_SIZE);
		File dbstore= new File(DB_PATH);
		this.existed = dbstore.exists();
		if (!this.existed) {
			try {
				dbstore.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.writer = new DataWriter(dbstore, this.existed);
		this.reader = new DataReader(dbstore);
	}

	@Override
	public boolean writeSchemaInfo(Schema schemaInfo,int pageNum) {
		ByteBuf block = new ByteBuf();
		block.write(schemaInfo.getPageId())
		     .write(schemaInfo.getSchemaName())
		     .write(schemaInfo.getUserName())
		     .write(schemaInfo.getPassWord())
		     .write(schemaInfo.getPages())
		     .write(schemaInfo.isHasTables());
		if(schemaInfo.isHasTables()) {
			block.writeByte((byte)schemaInfo.getTablesSize());
			schemaInfo.getTables().forEach((k,v)->{ block.write(k); block.write(v);});
		   }
		  this.writeBlock(pageNum, block);
		  LOG.info("Written schema on page num : " + pageNum);  
		 return true;
	}
	
	@Override
	public Schema getSchemaInfo(int pageNum) {
		ByteBuf b=this.readBlock(pageNum);
		Schema schemaInfo=new Schema();
		schemaInfo.setPageId(b.readInt());
		schemaInfo.setSchemaName(b.readString());
		schemaInfo.setUserName(b.readString());
		schemaInfo.setPassWord(b.readString());
        schemaInfo.setPages(b.readInt());
        schemaInfo.setHasTables(b.readBoolean());
        Map<String,Integer> tables=new HashMap<>();
		if(schemaInfo.isHasTables()) {
			byte tablesSize=b.readByte();
			schemaInfo.setTablesSize(tablesSize);
			for(int i=0;i<tablesSize;i++) {
				tables.put(b.readString(), b.readInt());	
			}
		}
		schemaInfo.setTables(tables);
		return schemaInfo;
	}

	@Override
	public boolean writeTable(Table table,int pageNum) {
		ByteBuf block=new ByteBuf(StoreConstants.BUFFER_SIZE);
		block.write(table.getName());
		block.write(table.getNum());
		block.write(table.getPrimary());
		block.write((short)table.getColumns().size());
		table.getColumns().forEach(column->{
			block.write(column.getName());
			block.writeByte(column.getPos());
			block.writeByte(column.getType().typeNum());
		});
		block.write(table.getRowByteSize());
		block.write(table.getIndexRoot());
		block.write(table.getRows());
        this.writeBlock(pageNum, block);	
        LOG.info("Writing table :"+table);
		return false;
	}

	@Override
	public Table getTable(int pageid) {
		 ByteBuf b=this.readBlock(pageid);
		 Table table=new Table(b.readString());
		 table.setNum(b.readInt());
		 table.setPrimary(b.readString());
		 short columnSize=b.readShort();
		 List<TableColumn> columns=new ArrayList<>();
		 for(int i=0;i<columnSize;i++) {
			 columns.add(new TableColumn(b.readString(), b.readByte(), Types.getType(b.readByte())));
		 }
		 table.setColumns(columns);
		 table.setRowByteSize(b.readShort());
		 table.setIndexRoot(b.readInt());
		 table.setRows(b.readInt());
		return table;
	}
	
	public void writeBlock(int blockNum, ByteBuf block) {
		int offset=blockNum*BLOCK_SIZE;
		writer.writeBlock(offset, block);
		writer.flush();
	 }
	
	public ByteBuf readBlock(int blockNum) {
		int offset=blockNum*BLOCK_SIZE;
		ByteBuf block=reader.readBlock(offset);
		return block;
	}
	
	public void writeIdxNode(BtreeNode node) {
		ByteBuf block = new ByteBuf(StoreConstants.BUFFER_SIZE);
		BtreeNodeType nodeType=node.isLeaf()?BtreeNodeType.LEAF:BtreeNodeType.INTERNAL;
		block.write(node.getId())// Page Number
		     .writeByte(nodeType.getNodVal())
		     .write(node.getParentId())
		     .write(node.getKeySize());//
		for (int i = 0; i < node.getKeySize(); i++) {
			block.write(node.keyAt(i));
			block.write(node.getChildId(i));
		}
		int offset = getNodeOffset(node.getId());
		//System.out.println("Writing page " + page.getId() + " at :" + offset);
		writer.writeBlock(offset, block);
		writer.flush();
	}
	
	public BtreeNode readIdxNode(int nodeId) {
		int offset = nodeId * BLOCK_SIZE;
		ByteBuf block = reader.readBlock(offset);
		int pageNum = block.readInt();
		byte type = block.readByte();
		BtreeNodeType nodeType=BtreeNodeType.getNodeType(type);
		boolean isLeaf = nodeType == BtreeNodeType.LEAF ? true : false;
		BtreeNode node = new BtreeNode(pageNum,isLeaf);
		//node.setId(pageNum);
		node.setParentId(block.readInt());
		int keySize = block.readInt();
		for (int i = 0; i < keySize; i++) {
			node.add(block.readInt(),block.readInt());
		}
		return node;
	}
	
	public int getNodeOffset(int pageNum) {
		int offset = pageNum * BLOCK_SIZE;
		return offset == -1 ? 0 : offset;
	}

	public boolean isExisted() {
		return existed;
	}

	public void setExisted(boolean existed) {
		this.existed = existed;
	}

	@Override
	public void addToBuffer(int key,BtreeNode node) {
		   bufferPages.put(key, node);
	   }

	@Override
	public String getPageList() {
		return null;
	}

	@Override
	public void close() throws IOException {
   	 }
	@Override
	public boolean writeTableRow() {
		return false;
	}
}
