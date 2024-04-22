package com.store.ds;

public enum Traversal {
	PRE("Pre"),IN("In"),POST("Post");
	 private Traversal(String text) {
      this.text=text;
	 }
	private String text;
	public String getText() {
		return text;
	}
}
