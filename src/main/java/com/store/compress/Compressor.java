package com.store.compress;

public interface Compressor {
	public byte[] compress(byte[] raw);
	public byte[] decompress(byte[] compressed);
}
