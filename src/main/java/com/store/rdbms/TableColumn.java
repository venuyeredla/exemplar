package com.store.rdbms;

public class TableColumn {
	private String name;
	private byte pos;
	private Types type;

	public TableColumn(String name, int pos, Types type) {
		this.name = name;
		this.pos = (byte)pos;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte getPos() {
		return pos;
	}
	public void setPos(byte pos) {
		this.pos = pos;
	}
	public Types getType() {
		return type;
	}
	public void setType(Types type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "TableColumn [name=" + name + ", pos=" + pos + ", type=" + type + "]";
	}
}
