	package com.store.rdbms;
import static com.store.io.StoreConstants.*;
import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.store.ds.Page;
import com.store.io.ByteBuf;
import com.store.io.DataReader;
import com.store.io.DataWriter;

/**
 * B-Tree implementation
 * 
 * @author vyeredla
 *
 */
public class BTree implements Closeable {
	private static final Logger LOG = LoggerFactory.getLogger(BTree.class);
	
	public Page root;
	private int degree = Page.degree;
	private DataWriter writer;
	private DataReader reader;
	private Schema schemaInfo = null;
	private List<Page> unsavedPages = null;

	public BTree(String dbFile) {
		
		File file= new File(dbFile);
		boolean exists =file.exists();
		this.writer = new DataWriter(file, exists);
		this.reader = new DataReader(file);
		unsavedPages = new ArrayList<>();
	}

	public void insert(int key, long data) {
		root = insert(root, key, data);
	}

	public Page insert(Page page, int key, long data) {
		if (page == null) {
			page = newPage(true); // new Page(nextPageId(),false);
			page.addKey(key);
			return page;
		} else {
			if (page.isFull()) { // && page.isLeaf()
				Page newParent = newPage(false); // new Page(nextPageId(),false);
				newParent.setChild(0, page);
				splitChild(newParent, page, 0);
				int i = 0;
				if (newParent.getKey(0) < key)
					i++;
				insertNotFull(newParent.getChild(i), key);
				return newParent; // page = newParent;
			} else {
				insertNotFull(page, key);
				return page;
			}
		}
	}

	/**
	 * Key is inserted in node or page which is not full.
	 * 
	 * @param page
	 * @param key
	 * @return
	 */
	private Page insertNotFull(Page page, int key) {
		try {
			int i = page.getKeySize() - 1;
			if (page.isLeaf() == true) {
				page.insertKey(key);
				return page;
			} else {// if this is not a leaf node.
				while (i >= 0 && page.getKey(i) > key)
					i--;
				int childPageId = page.getChildId(i + 1);
				Page requiredChild = getPage(childPageId);
				if (requiredChild.isFull()) {
					splitChild(page, requiredChild, i + 1);
					if (page.getKey(i + 1) < key)
						i++;
				}
				Page newRequired = getPage(page.getChildId(i + 1));
				return insertNotFull(newRequired, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Exception in inserting key : " + key + " Page Number : " + page.getId());
		}
		return page;

	}

	public void splitChild(Page parent, Page child, int index) {
		parent.setLeaf(false);
		Page newChild = newPage(child.isLeaf()); // new Page(nextPageId(),child.isLeaf());
		for (int k = 0; k < degree - 1; k++) { // copying second half of keys and childs to new child.
			newChild.addKey(child.deleteKey(k + degree));
		   }
		
		for(int k=0;k<degree;k++) {    //copying second half childs to new child.
			Page popChild=child.deleteChild(k + degree);
			if(popChild!=null) {
				newChild.setChild(k, popChild);
			}
		  }
		parent.insertKey(child.deleteKey(degree - 1));
		for (int i = parent.getKeySize() - 1; i >= index + 1; i--) {
			parent.moveChild(i, i + 1);
		}
		parent.setChild(index + 1, newChild);
		newChild.setHalfFill();
		child.setHalfFill();
	}

	public void traverse(Page page) {
		if (page != null) {
			System.out.println("Page:" + page.getId() + "&key size:" + page.getKeySize() + "  --" + page.getKeys());
			int i;
			for (i = 0; i < page.getKeySize(); i++) {
				if (page.isLeaf() == false) {
					Page child=getPageTravarse(page.getChildId(i));
					traverse(child);
				 }
					
				// System.out.print(page.keys[i] + ",");
			}
			if (page.isLeaf() == false)
				traverse(page.getChild(i));
		}
	}

	public Page search(Page page, int key) {
		int position = 0;
		try {
			int i = 0;
			while (i < page.getKeySize() && key > page.keys[i]) {
				i++;
			}
			position = page.keys[i];
			if (page.keys[i] == key)
				return page;

			if (page.isLeaf() == true) {
				return null;
			}
			Page childePage=getPageTravarse(page.getChildId(i));
			return search(childePage, key);

		} catch (Exception e) {
			System.out.println("Errop Page:" + page.getId() + " index" + position);
		}
		return null;
	}

	public void delete() {

	}

	public void writePage(Page page) {
		ByteBuf block = new ByteBuf();
		int pageType = page.isLeaf() ? 2 : 1;
		block.write(page.getId());// Page Number
		block.write((byte) pageType);
		if (page.getParent() != null) {
			block.write(page.getParent().getId());// Parent Page Number
		} else {
			block.write(-1);// Parent Page Number
		}
		block.write(page.getKeySize());//
		for (int i = 0; i < page.getKeySize(); i++) {
			block.write(page.getKey(i));
		}
		block.write(page.getChildSize());// ChildPages
		for (int i = 0; i < page.getChildSize(); i++) {
			block.write(page.getChildId(i));
		}
		int offset = getPageOffset(page.getId());
		//System.out.println("Writing page " + page.getId() + " at :" + offset);
		writer.writeBlock(offset, block);
		writer.flush();
	}

	public Page newPage(boolean isLeaf) {
		Page newPage = new Page(nextPageId(), isLeaf);
		unsavedPages.add(newPage);
		return newPage;
	}

	
	
	public Page getPageTravarse(int pageid) {
		if (pageid == -1) {
			return null;
		} else if (pageid <= schemaInfo.getPages()) {
			Page rpage=readPage(pageid);
			unsavedPages.add(rpage);
			return rpage;
     	}
		return null;
	}
	
	
	
	public Page getPage(int pageid) {
		for (Page page : unsavedPages) {
			if (page.getId() == pageid) {
				return page;
			}
		}
		if (pageid == -1) {
			return null;
		} else if (pageid <= schemaInfo.getPages()) {
			Page rpage=readPage(pageid);
			unsavedPages.add(rpage);
			return rpage;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Page readPage(int pageId) {
		int offset = pageId * BLOCK_SIZE;
		ByteBuf block = reader.readBlock(offset);
		int pageNum = block.readInt();
		byte b = block.readByte();
		boolean isLeaf = b == 2 ? true : false;
		Page page = new Page(pageNum, isLeaf);
		page.setId(pageNum);
		page.setParentId(block.readInt());
		int keySize = block.readInt();
		for (int i = 0; i < keySize; i++) {
			page.addKey(block.readInt());
		}
		int childSize = block.readInt();
		for (int i = 0; i < childSize; i++) {
			page.setChildId(i, block.readInt());
		}
		return page;
	}

	public int getPageOffset(int pageNum) {
		int offset = pageNum * BLOCK_SIZE;
		return offset == -1 ? 0 : offset;
	}

	public int nextPageId() {
		return schemaInfo.nextPage();
	}

	public void closeWriter() {
		this.writer.close();
	}

	public void closeReader() {
		this.reader.close();
	}

	@Override
	public void close() {
		unsavedPages.forEach(page -> {
			this.writePage(page);
		});
		//this.writeMeta();
		/*
		 * this.writer.close(); this.reader.close();
		 */
	}
}
