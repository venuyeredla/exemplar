package com.store.ds;

import com.store.io.ByteBuf;
import com.store.io.DataReader;
import com.store.io.DataWriter;

/**
 * BST balanced using AVL Tree concept.
 * @author venugopal
 *
 */
public class Bst {
	private Node root;
	private static int totalNodes=0;
	public Bst() {	}
	
	public void insert(int key,long pointer) {
	    root=insert(root,key, pointer);
	  }
  private Node insert(Node node,int key,long data) {
	if(node==null){
		return newNode(key,data); 
	}if(key<node.key) {
		node.left=insert(node.left, key,data);
	}else if(key>node.key) {
		node.right=insert(node.right, key,data);
	}else 	return node;
	
	 node.height = 1 + max(height(node.left),height(node.right));
	 int balance = getBalance(node);
 
	 if (balance > 1 && key < node.left.key)
        return rightRotate(node);
    // Right Right Case
    if (balance < -1 && key > node.right.key)
        return leftRotate(node);
    // Left Right Case
    if (balance > 1 && key > node.left.key) {
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }
    // Right Left Case
    if (balance < -1 && key < node.right.key) {
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }

	return node;
	
	}
	
  
  int getBalance(Node N) {
      if (N == null)
          return 0;

      return height(N.left) - height(N.right);
  }
  // A utility function to right rotate subtree rooted with y
  // See the diagram given above.
  Node rightRotate(Node y) {
      Node x = y.left;
      Node T2 = x.right;

      // Perform rotation
      x.right = y;
      y.left = T2;

      // Update heights
      y.height = max(height(y.left), height(y.right)) + 1;
      x.height = max(height(x.left), height(x.right)) + 1;

      // Return new root
      return x;
  }

  // A utility function to left rotate subtree rooted with x
  // See the diagram given above.
  Node leftRotate(Node x) {
      Node y = x.right;
      Node T2 = y.left;

      // Perform rotation
      y.left = x;
      x.right = T2;

      //  Update heights
      x.height = max(height(x.left), height(x.right)) + 1;
      y.height = max(height(y.left), height(y.right)) + 1;

      // Return new root
      return y;
  }
	
  
  int max(int a, int b) {
      return (a > b) ? a : b;
  }
  
  
  
	public long search(int key) {
		Node node=search(root, key);
		return node!=null?node.data:-1;
	}
	
	private Node search(Node node,int key) {
		return node==null?null:node.key==key?node:key<node.key?this.search(node.left, key):this.search(node.right, key);
	}

	public boolean delete(int key) {
		delete(root, key);
		return true;
	  }
	
	private Node delete(Node node, int key) {
		  if(key<node.key) {
			  node.left=delete(node.left, key);
		  }else if(key>node.key) {
			  node.right=delete(node.right, key);
		  }else {
			  if(node.left==null) {
				  return node.right;
			  }else if(node.right==null) {
				  return node.left;
			  }
			  node.key=minKey(node.right);
			  node.right=delete(node.right, node.key);
		  }
		return node;
	}
	
	
	private int minKey(Node node) {
		 int minKey=node.key;
	     while (node.left !=null)
	      {
	         node = node.left;
	         minKey=node.key;
	      }
	     return minKey;
	     
	}
	
	private static Node newNode(int key,long pointer) {
		totalNodes++;
		return new Node(key,pointer);
	}
	
		
	private int height(Node node) {
		return node!=null?node.height:0;
	}
	
	public void traverse(Traversal mode) {
		System.out.print("\n"+mode.getText()+":");
		switch (mode) {
		case PRE:
			preOrder(root,null);
			break;
		case POST:
			postOrder(root);
			break;
		case IN:
			inOrder(root);
			break;
		default:
			
			break;
		}
	}
	
	public void inOrder(Node node) {
		if(node!=null) {
			if(node.left!=null) inOrder(node.left);
			System.out.print(node.key+",");
			if(node.right!=null) inOrder(node.right); 
		 }
	}
	public void preOrder(Node node,ByteBuf bytes) {
		if(node!=null) {
			    if(bytes==null) {
			    	System.out.print(node.key+", ");	
			    }else {
			    	 bytes.write(node.key);bytes.write((int) node.data);
			    }
				if(node.left!=null) preOrder(node.left,bytes);
				if(node.right!=null) preOrder(node.right,bytes); 
			 }
	}
	
	public void postOrder(Node node) {
		if(node!=null) {
			if(node.left!=null) postOrder(node.left);
			if(node.right!=null) postOrder(node.right); 
			System.out.print(node.key+",");
		 }
	}

	public void writeToStorage(DataWriter dataWriter) {
		ByteBuf block=new ByteBuf(4096);
		block.write("BST#pre");
		block.writeVInt(totalNodes);
		preOrder(root,block);
		dataWriter.writeBlock(0, block);
		dataWriter.close();
	}
	
	public static Bst readFromStorage(DataReader reader) {
		ByteBuf block=reader.readBlock(0);
		Bst bst=new Bst();
		String codec=block.readString();
		System.out.println("BST index prefix: "+codec);
		int totalNodes=block.readVInt();
		for(int i=0;i<totalNodes;i++) {
			 int key=block.readInt();
			 int pointer= block.readInt();
			 bst.insert(key,pointer);
		}
		return bst;
	}
	
	static class Node{
		private int key, height;
		private long data;
		private Node left,right;
        public Node() {  }
		public Node(int key) {
			this.key = key;
			this.height=1;
		}
		public Node(int key,long pointer) {
			this.key = key;
			this.height=1;
			this.data=pointer;
		}
	}

}