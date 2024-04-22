package com.store.rdbms;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * B+ tree index, internal nodes contain index data & and leaves will hold
 * actual data.
 * 
 * @author vyeredla
 *
 */
public class BTreeIndex implements Closeable {
	private static final Logger LOG = LoggerFactory.getLogger(BTreeIndex.class);
	private Store store = null;
	public BtreeNode root;
	private int degree = DBConstatnts.DEGREE;
	private Schema schemaInfo = null;
	private Table table;
	Map<Integer, BtreeNode> nodeBuffer = null;
	
	public BTreeIndex(Schema schemaInfo, Table table, Store store) {
		this.schemaInfo=schemaInfo;
		this.store = store;
		this.table=table;
		this.nodeBuffer = new HashMap<>();
		if (table.getIndexRoot() != -1) {
			root =this.getNode(table.getIndexRoot());
			LOG.info("Index existed. (Table, root)=("+this.root.getId()+","+table.getIndexRoot()+")");
		} else {
			LOG.info("Index didn't exist");
			root = newNode(true);
			table.setIndexRoot(this.root.getId());
		 }
	  }

	public void insert(int key, int pageid) {
		if (root.isLeaf() && root.isFull()) {
			BtreeNode newRoot = newNode(false);
			splitChild(newRoot, 0, root, key, pageid);
			table.setIndexRoot(newRoot.getId());
			this.root = newRoot;
		} else {
			insertNotFull(null, root, key, pageid);
		}
	 }
	
	
	private BtreeNode insertNotFull(BtreeNode parent, BtreeNode node, int key, int nodeid) {
		try {
			if (node.isLeaf() == true) {
				int maxKey = node.insert(key, nodeid);
				if (parent != null && key >= maxKey) {
					int childPos = parent.getChildPos(node.getId());
					if (childPos != -1) {
						parent.setKey(childPos, key);
					}
				}
				return node;
			} else {
				int i = node.getKeySize() - 1;
				while (i >= 0 && key < node.keyAt(i))
					i--;
				if (++i == node.getKeySize())
					i--;
				BtreeNode requiredChild = this.getChild(node, i);
				LOG.info("Insert (key,index)  : (" + key + "," + (i) + ") --:  " + requiredChild.keys());
				if (requiredChild.isLeaf() && requiredChild.isFull()) {
					splitChild(node, i, requiredChild, key, nodeid);
				} else {
					return insertNotFull(node, requiredChild, key, nodeid);
				}
			}
		} catch (Exception e) {
			LOG.info("Exception in inserting (key, in page)=(" + key + "," + node.getId() + ") - Keys" + node.keys());
			e.printStackTrace();
		}
		return node;
	}

	/**
	 * Splits the child and insert element in right child.and calls update parent
	 * method.
	 * 
	 * @param parent
	 * @param index
	 * @param child
	 * @param key
	 * @param pageid
	 */
	public void splitChild(BtreeNode parent, int index, BtreeNode child, int key, int pageid) {
		BtreeNode childNew = this.splitInsert(child, key, pageid);
		if (parent.isNotFull()) {
			parent.insert(index, child.maxKey(), child.getId());
			parent.insert(childNew.maxKey(), childNew.getId());
			child.setParentId(parent.getId());
			childNew.setParentId(parent.getId());
			this.updateParents(parent);
		} else {
			parent.insert(index, child.maxKey(), child.getId());
			child.setParentId(parent.getId());
			this.updateParents(parent);
			splitParent(parent, childNew);
		}
	}

	/**
	 * Splits the parent and inserts the child in righ parent.
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	public void splitParent(BtreeNode parent, BtreeNode child) {
		BtreeNode parentNew = this.splitInsert(parent, child.maxKey(), child.getId());
		if (child.maxKey() < parent.maxKey()) {
			child.setParentId(parent.getId());
		} else {
			child.setParentId(parentNew.getId());
		}

		BtreeNode parentParent = this.getParent(parent);
		if (parentParent == null) {
			parentParent = newNode(false);
			this.root = parentParent;
			table.setIndexRoot(this.root.getId());
		}
		int index = parentParent.getChildPos(parent.getId());
		index = index == -1 ? 0 : index; // -1 means new root
		if (parentParent.isNotFull()) {
			parentParent.insert(index, parent.maxKey(), parent.getId());
			parentParent.insert(parentNew.maxKey(), parentNew.getId());
			parent.setParentId(parentParent.getId());
			parentNew.setParentId(parentParent.getId());
			this.updateParents(parentParent);
		} else {
			parentNew.insert(index, child.maxKey(), child.getId());
			child.setParentId(parentParent.getId());
			this.updateParents(parent);
			splitParent(parentParent, parentNew);
		}
	}

	public BtreeNode splitInsert(BtreeNode node, int key, int value) {
		BtreeNode nodeNew = newNode(node.isLeaf());
		int firstMax = node.keyAt(degree - 1);
		for (int k = 0; k < degree; k++) { // Copying keys to second child
			nodeNew.insert(node.deleteKey(k + degree), node.deleteChild(k + degree));
		}
		if (key < firstMax) {
			node.insert(key, value);
		} else {
			nodeNew.insert(key, value);
		}
		return nodeNew;
	}

	public void updateParents(BtreeNode node) {
		BtreeNode parent = this.getParent(node);
		if (parent != null) {
			int index = parent.getChildPos(node.getId());
			parent.setKey(index, node.maxKey());
			this.updateParents(parent);
		}
	}

	public void traverse() {
		LOG.info("Traversing tree ");
		this.traverse(root);
	}

	private void traverse(BtreeNode node) {
		if (node != null) {
			// LOG.info(page.keys()+ "Childs : "+page.childs());
			// if(!node.isLeaf())
			System.out.println(node.keys() + "  " + node.childs());
			int i;
			try {
				if (node.isLeaf() == false) {
					for (i = 0; i < node.getKeySize(); i++) {
						BtreeNode child = getChild(node, i);
						traverse(child);
					}
				}
			} catch (Exception e) {
				e.getSuppressed();
			}
		}
	}
	
	
	public int search(int key) {
		int pageid=this.search(this.root, key);
		return pageid;
	
	}
	

	private int search(BtreeNode node, int key) {
		try {
			if(node.isLeaf()) {
               return node.seach(key);				
			}
			int i = 0;
			while (i < node.getKeySize() && key > node.keyAt(i)) {
				i++;
			 }
			BtreeNode childePage = this.getNodeSearch(node.getChildId(i));
			return search(childePage, key);
		} catch (Exception e) {
			System.out.println("Error in getting  Page:" + node.getId() + " index : ");
		}
		return -1;
	}

	public void delete() {
	}

	private BtreeNode getNodeSearch(int id) {
		return store.readIdxNode(id);
	}
	
	
	private BtreeNode getNode(int nodeid) {
		if(nodeBuffer.containsKey(nodeid)) {
			return nodeBuffer.get(nodeid);
		}else {
			BtreeNode node=store.readIdxNode(nodeid);
			this.nodeBuffer.put(node.getId(), node);
			return node;
		}
	}
	
	private BtreeNode getParent(BtreeNode node) {
		if (node.getParentId() != -1) {
			return this.getNode(node.getParentId()) ;
		}
		return null;
	 }

	private BtreeNode getChild(BtreeNode node, int pos) {
		int childPage = node.getChildId(pos);
		return this.getNode(childPage);
	}

	private BtreeNode newNode(boolean isLeaf) {
		int nodeid = this.nextPageId();
		BtreeNode node = new BtreeNode(nodeid, isLeaf);
		this.nodeBuffer.put(node.getId(), node);
		return node;
	}
	
	private int nextPageId() {
		return schemaInfo.nextPage();
	}

	public int getRootPageId() {
		return this.root.getId();
	}
	
	@Override
	public void close() throws IOException {
		nodeBuffer.forEach((key,val)->{
			store.writeIdxNode(val);
		});
	}
	
}
