/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.io;

import java.io.*;
import douglas.mencken.util.ByteTransformer;

/**
 *	<code>ByteArrayOStream</code>
 *
 *	@version 1.26f2
 */

public class ByteArrayOStream extends OutputStream implements ObjectOutput {
	
	/** Number of valid bytes in buffer. */
	protected int count;
	
	protected byte[] buf;
	
	/**
	 *	Default buffer size is 32.
	 */
	public ByteArrayOStream() {
		this(32);
	}
	
	public ByteArrayOStream(int size) {
		this.buf = new byte[size];
	}
	
	public void write(int b) {
		this.writeByte(b);
	}
	
	public void write(byte b[], int off, int len) {
		int newcount = count + len;
		if (newcount > buf.length) {
			byte[] newbuf = new byte[Math.max(buf.length << 1, newcount)];
			System.arraycopy(buf, 0, newbuf, 0, count);
			this.buf = newbuf;
		}
		
		System.arraycopy(b, off, buf, count, len);
		this.count = newcount;
	}
	
	/**
	 *	Writes a <code>boolean</code> as a 1-byte value.
	 */
	public void writeBoolean(boolean v) {
		this.write(v ? 1 : 0);
	}
	
	/**
	 *	Writes a <code>byte</code>.
	 */
	public void writeByte(int b) {
		int newcount = count + 1;
		if (newcount > buf.length) {
			byte[] newbuf = new byte[Math.max(buf.length << 1, newcount)];
			System.arraycopy(buf, 0, newbuf, 0, count);
			buf = newbuf;
		}
		buf[count] = (byte) b;
		count = newcount;
	}
	
	/**
	 *	Writes a <code>short</code>.
	 */
	public void writeShort(int v) {
		write(ByteTransformer.extractBytesFromShort(v), 0, 2);
	}
	
	/**
	 *	Writes a <code>char</code>.
	 */
	public void writeChar(int v) {
		this.writeShort(v);
	}
	
	/**
	 *	Writes an <code>int</code>.
	 */
	public void writeInt(int b) {
		write(ByteTransformer.extractBytesFromInt(b), 0, 4);
	}
	
	/**
	 *	Writes a <code>long</code>.
	 */
	public void writeLong(long l) {
		write(ByteTransformer.extractBytesFromLong(l), 0, 8);
	}
	
	/**
	 *	Writes a <code>float</code>.
	 */
	public void writeFloat(float f) {
		write(ByteTransformer.extractBytesFromFloat(f), 0, 4);
	}
	
	/**
	 *	Writes a <code>double</code>.
	 */
	public void writeDouble(double d) {
		write(ByteTransformer.extractBytesFromDouble(d), 0, 8);
	}
	
	/**
	 *	Writes a string as a sequence of bytes. Each character in the string
	 *	is written out, in sequence, by discarding its high eight bits.
	 */
	public void writeBytes(String string) {
		int length = string.length();
		for (int i = 0; i < length; i++) {
			this.write((byte)string.charAt(i));
		}
    }
    
    /**
	 *	Writes a string as a sequence of characters. Each character
	 *	is written out as if by the <code>writeChar</code> method.
	 */
	public void writeChars(String string) {
		int length = string.length();
		for (int i = 0; i < length; i++) {
			this.writeChar(string.charAt(i));
		}
    }
    
	/**
	 *	Writes a string using UTF-8 encoding in a machine-independent manner.
	 */
	public void writeUTF(String str) throws UTFDataFormatException {
		int strlen = str.length();
		int utflen = 0;
		
		for (int i = 0 ; i < strlen; i++) {
			int c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}
		
		if (utflen > 65535) throw new UTFDataFormatException();
		
		write((utflen >>> 8) & 0xFF);
		write((utflen >>> 0) & 0xFF);
		
		for (int i = 0 ; i < strlen; i++) {
			int c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				write(c);
			} else if (c > 0x07FF) {
				write(0xE0 | ((c >> 12) & 0x0F));
				write(0x80 | ((c >>	6) & 0x3F));
				write(0x80 | ((c >>	0) & 0x3F));
			} else {
				write(0xC0 | ((c >>	6) & 0x1F));
				write(0x80 | ((c >>	0) & 0x3F));
			}
		}
	}
	
	public void writeObject(Object obj) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(this);
		out.writeObject(obj);
	}
	
	public byte[] toByteArray() {
		byte[] newbuf = new byte[count];
		System.arraycopy(this.buf, 0, newbuf, 0, count);
		return newbuf;
	}
	
	/**
	 *	Closes this stream and releases any resources associated
	 *	with this stream. 
	 */
	public void close() {
		this.reset();
	}
	
	public synchronized void reset() {
		this.count = 0;
		this.buf = new byte[0];
	}
	
	public int getSize() { return this.count; }
	
}