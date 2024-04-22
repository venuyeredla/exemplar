package com.store.rdbms;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private int pageid;
	private String name;
	private int num;
	private String primary;
	private List<TableColumn> columns;
	private short rowByteSize;
	private int indexRoot;
	private int rows;
	
	public Table(String name) {
		this.name = name;
		this.indexRoot=-1;
		columns=new ArrayList<>();
	}

	public short rowByteSize() { 
		int rowByteSize=columns.stream().mapToInt(col->col.getType().byteSize()).sum();
		return (short)rowByteSize;
	}
	
	public boolean addColumn(TableColumn col) {
		return this.columns.add(col);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public List<TableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}

	public short getRowByteSize() {
		return rowByteSize;
	}

	public void setRowByteSize(short rowByteSize) {
		this.rowByteSize = rowByteSize;
	}
	
	public int getIndexRoot() {
		return indexRoot;
	}

	public void setIndexRoot(int indexRoot) {
		this.indexRoot = indexRoot;
	}
	
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void inrcreRowSize() {
		this.rows++;
	}

	public int getPageid() {
		return pageid;
	}

	public void setPageid(int pageid) {
		this.pageid = pageid;
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ", num=" + num + ", primary=" + primary + ", columns=" + columns + ", indexRoot="
				+ indexRoot + "]";
	}
}
