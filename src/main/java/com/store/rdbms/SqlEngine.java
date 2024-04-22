package com.store.rdbms;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.store.io.ByteBuf;

public class SqlEngine implements Closeable {
	private static final Logger LOG = LoggerFactory.getLogger(SqlEngine.class);
	
	private StoreToFile fileStore=null;
	private Schema schema=null;
	private static int SCHEMA_PAGE_ID=0;
	private Map<String, BTreeIndex> tableIndexers=new HashMap<>();
	private Map<String, Table> tables=new HashMap<>();
	
	public SqlEngine(String dbname, String userName, String passWord, boolean ifNot) {
		  LOG.info("DB engine bootstrapping ");
		  fileStore=new StoreToFile(dbname, userName, passWord,ifNot);	
		  initSchema(dbname, userName, passWord);
	}
	
	private void initSchema(String schemaName, String userName, String passWord) {
		 boolean existed=fileStore.isExisted();
		 if(existed) {
			 LOG.info("Loading existing schema.");
			 schema=fileStore.getSchemaInfo(SCHEMA_PAGE_ID);// loads existing schema 
		 }else {
			 schema=new Schema(schemaName, userName, passWord);
			 fileStore.writeSchemaInfo(this.schema, SCHEMA_PAGE_ID);
			 fileStore.setExisted(true);
			 LOG.info("New scheama created. Name : "+schema);
		 }
	 }
	
	public void createTable(Table table) {
		 if(!schema.contains(table.getName())) {
			 table.setNum(schema.nextTableId());
			 int pageNum=schema.nextPage();
			 fileStore.writeTable(table, pageNum);
			 schema.addTable(table.getName(), pageNum);
			 schema.setUpdated(true);
			 LOG.info("Table created : "+table.getName() +" Page number : "+pageNum);
		 }else {
			 LOG.info("Table already existed : "+table.getName());
		 }
	 }

	public Table getTable(String tableName) {
		int pageNum=schema.tablePage(tableName);
		if(pageNum!=-1) {
			if (tables.containsKey(tableName)) {
				return tables.get(tableName);
			}else {
				Table table=fileStore.getTable(pageNum);
				table.setPageid(pageNum);
				LOG.info("Table loaded : "+table);
				tables.put(tableName, table);
				return table;
			}
			
		}else {
			LOG.info("Table doesn't exist : "+tableName);
			return null;
		}
	}
	
	public BTreeIndex getBtreeIndex(Table table) {
		BTreeIndex bTreeIndex=tableIndexers.get(table.getName());
		if(bTreeIndex!=null) {
			return bTreeIndex;
		}else {
			bTreeIndex=new BTreeIndex(this.schema, table,fileStore);;
			tableIndexers.put(table.getName(), bTreeIndex);
		}
		return bTreeIndex;
	}
	
	public void insert(String tableName,TableRow row) {
		Table table=this.getTable(tableName);
		BTreeIndex bTreeIndex=getBtreeIndex(table);
		ByteBuf bytes=new ByteBuf();
		if(table!=null) {
			table.getColumns().forEach(column->{
			   String name= column.getName();
			   Types type=column.getType();
	           row.wirteColumn(bytes, name, type);
			});
			int pageid=this.nextPage();
			fileStore.writeBlock(pageid, bytes);
			int key=row.getId();
			bTreeIndex.insert(key, pageid);
			table.inrcreRowSize();
			LOG.info("New row added to the table :"+table.getName());
		}
	}
	
	public TableRow select(String tableName,int id) {
		Table table=this.getTable(tableName);
		BTreeIndex bTreeIndex=new BTreeIndex(this.schema,table,fileStore);
		int pageid=bTreeIndex.search(id);
		if(pageid!=-1) {
			ByteBuf bytes=fileStore.readBlock(pageid);
			TableRow tableRow=new TableRow();
			table.getColumns().forEach(column->{
				   String name= column.getName();
				   Types type=column.getType();
				   tableRow.addColumn(bytes, name, type);
			});
			 LOG.info("Selected a row :"+table.getName());
			 try {
				bTreeIndex.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 return tableRow;
		}
		try {
			bTreeIndex.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@Override
	public void close() throws IOException {
		LOG.info("Shtting down DB. ");
		tableIndexers.forEach((key,tableIndexer)->{
			try {
				tableIndexer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		if(schema.isUpdated()) {
			fileStore.writeSchemaInfo(this.schema, SCHEMA_PAGE_ID);
			tables.forEach((key,val)->{	fileStore.writeTable(val, val.getPageid());	});
		}
		
		fileStore.close();
		LOG.info("Shtting down completed. ");
	}
	
	private int nextPage() {
	   return schema.nextPage();
	 }
	
}
