package com.store.rdbms;

import java.util.HashMap;
import java.util.Map;

public class Schema {
	private int pageId = 0;
	private String name;
	private String user;
	private String pass;
	private int pages;
	private boolean hasTables;
	private short tablesSize;
	private Map<String, Integer> tables;
	private boolean existed;
	private boolean updated;
	
	public Schema () {
		
	}
	
	/**
	 * Creates new schema and saves to the disk
	 * 
	 * @param schemaName
	 * @param userName
	 * @param passWord
	 */
	public Schema(String name, String userName, String passWord) {
		this.pageId = 0;
		this.name = name;
		this.user = userName;
		this.pass = passWord;
		this.pages = 1;
		this.hasTables = false;
		this.tablesSize = 0;
		tables = new HashMap<>();
	}

	/**
	 * Constructs the SchemaInfo by from the block of memory.
	 * 
	 * @param block
	 */

	public int nextPage() {
		return pages++;
	}

	public int nextTableId() {
		return tablesSize++;
	}

	public boolean contains(String name) {
		return tables.keySet().contains(name);
	}

	public void addTable(String name, int pageid) {
		tables.put(name, pageid);
		this.hasTables = true;
	}

	public int tablePage(String tableName) {
		if (tables.keySet().contains(tableName)) {
			return tables.get(tableName);
		}
		return -1;
	}

	public String getSchemaName() {
		return name;
	}

	public void setSchemaName(String schemaName) {
		this.name = schemaName;
	}

	public String getUserName() {
		return user;
	}

	public void setUserName(String userName) {
		this.user = userName;
	}

	public String getPassWord() {
		return pass;
	}

	public void setPassWord(String passWord) {
		this.pass = passWord;
	}

	public boolean isHasTables() {
		return hasTables;
	}

	public void setHasTables(boolean hasTables) {
		this.hasTables = hasTables;
	}

	public Map<String, Integer> getTables() {
		return tables;
	}

	public void setTables(Map<String, Integer> tables) {
		this.tables = tables;
	}

	public boolean isExisted() {
		return existed;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public void setExisted(boolean existed) {
		this.existed = existed;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public short getTablesSize() {
		return tablesSize;
	}

	public void setTablesSize(short tablesSize) {
		this.tablesSize = tablesSize;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}
	
	
	

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "SchemaInfo [pageId=" + pageId + ", name=" + name + ", user=" + user + ", pass=" + pass + ", pages="
				+ pages + ", hasTables=" + hasTables + ", tablesSize=" + tablesSize + ", tables=" + tables
				+ ", existed=" + existed + "]";
	}
}
