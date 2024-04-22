package com.store.io;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to write data in terms of blocks
 * 
 * @author Author
 *
 */
public class DataWriter implements Closeable {
	private static final Logger LOG = LoggerFactory.getLogger(DataWriter.class);
	
	private OutputStream os = null;
	private File file;
	private int bytesWritten = 0;

	/**
	 * @param fileName
	 * @param append  -- if false creates new file.
	 */
	public DataWriter(File file, boolean append) {
		try {
			this.file = file;
			this.os = new BufferedOutputStream(new FileOutputStream(this.file, append));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOG.error("Error in opening file ", e);
		}
	}
	
	/**
	 * @param fileName
	 * @param append  -- if false creates new file.
	 */
	public DataWriter(String file, boolean append) {
		try {
			this.os = new BufferedOutputStream(new FileOutputStream(this.file, append));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOG.error("Error in opening file ", e);
		}
	}

	public DataWriter(OutputStream os) {
		this.os = new BufferedOutputStream(os);
	}

	public void writeBlock(int offset, ByteBuf block) {
		try {
			Path path = FileSystems.getDefault().getPath(this.file.getAbsolutePath());
			SeekableByteChannel sbc = Files.newByteChannel(path, StandardOpenOption.WRITE);
			sbc.position(offset);
			ByteBuffer byteBuffer = ByteBuffer.wrap(block.getBytes());
			sbc.write(byteBuffer);
			sbc.close();
			//LOG.info("No of bytes written : "+block.getBytes().length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeBytes(byte[] bytes) {
		try {
			bytesWritten = bytes.length;
			this.os.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getFilePointer() {
		return bytesWritten;
	}

	public void update(int offset, byte[] bytes) {
		try {
			Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
			SeekableByteChannel sbc = Files.newByteChannel(path, StandardOpenOption.WRITE);
			sbc.position(offset);
			ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
			sbc.write(byteBuffer);
			sbc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void flush() {
		try {
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
				os.flush();
				os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
