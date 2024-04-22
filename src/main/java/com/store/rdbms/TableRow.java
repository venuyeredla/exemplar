package com.store.rdbms;

import java.util.HashMap;
import java.util.Map;

import com.store.io.ByteBuf;

public class TableRow {
	private Map<String,Object> colums;

	public TableRow() {
		this.colums = new HashMap<>();
	}
	
	public int getId() {
		return (int) colums.get("id");
	}
	
	public void addColumn(String key,Object value) {
		  colums.put(key, value);
	}
	
	public void addColumn(ByteBuf bytes,String key,Types type) {
		   switch (type) {
		case BYTE:
			    this.addColumn(key,  bytes.readByte());
			break;
		case SHORT:
			this.addColumn(key,  bytes.readShort());
			break;
		case INT:
			this.addColumn(key,  bytes.readInt());
			break;
		case LONG:
			this.addColumn(key,  bytes.readLong());
			break;
		case STRING:
			this.addColumn(key,  bytes.readString());
			break;
		default:
			break;
		}
	}
	
	public void wirteColumn(ByteBuf bytes,String key,Types type) {
	   Object Obj= colums.get(key);
	   switch (type) {
	case BYTE:
		  bytes.writeByte((byte) Obj);
		break;
	case SHORT:
		 bytes.write((short) Obj);
		break;
	case INT:
		bytes.write((int) Obj);
		break;
	case LONG:
		bytes.writeLong((long) Obj);
		break;
	case STRING:
		bytes.write((String)Obj);
		break;
	default:
		break;
	}
	   
	}

	@Override
	public String toString() {
		return "TableRow [colums=" + colums + "]";
	}
	
}
