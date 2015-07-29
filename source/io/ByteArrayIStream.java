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
 *	<code>ByteArrayIStream</code>
 *
 *	@version 1.45f3
 */

public class ByteArrayIStream extends InputStream implements ObjectInput {
	
	protected int meter = 0;
	protected byte[] dataToRead = new byte[0];
	protected boolean closed = true;
	
	public ByteArrayIStream(byte[] data) {
		this(data, 0);
	}
	
	public ByteArrayIStream(byte[] data, int meter) {
		if (data == null) {
			throw new IllegalArgumentException(
				super.getClass().getName() + ": can't read 'null'"
			);
		}
		
		this.closed = false;
		
		this.meter = meter;
		this.dataToRead = data;
	}
	
	/**
	 *	Returns the current offset in this stream.
	 */
	public int getStreamPointer() {
		return this.meter;
	}
	
	public int available() {
		return (dataToRead.length - meter);
	}
	
	public void reset() {
		this.meter = 0;
	}
	
	public void close() {
		this.reset();
		
		this.dataToRead = new byte[0];
		this.closed = true;
	}
	
	/**
	 *	Skips over and discards <code>n</code> bytes of data.
	 *	The actual number of bytes skipped is returned.
	 *
	 *	@param	n	the number of bytes to be skipped.
     *	@return		the actual number of bytes skipped.
     */
	public long skip(long n) throws IOException {
		if (!this.closed) {
			return (long)(this.skipBytes((int)n));
		}
		
		throw new IOException("The stream is closed");
	}
	
	public int skipBytes(int n) throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		int oldMeter = this.meter;
		
		this.meter += n;
		if (this.meter >= dataToRead.length) {
			this.meter = dataToRead.length - 1;
		}
		
		return (this.meter - oldMeter);
	}
	
	public void readFully(byte[] b) throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		this.readFully(b, 0, b.length);
	}
	
	public void readFully(byte[] b, int off, int len) throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			for (int n = 0; n < len; n++) {
				b[off+n] = this.readByte();
			}
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	/**
	 *	Reads a signed 8-bit number (<code>byte</code>).
	 */
	public byte readByte() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			byte ret = dataToRead[meter];
			meter++;
			return ret;
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	/**
	 *	Reads a <code>boolean</code>.
	 */
	public boolean readBoolean() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			int v = this.read();
			return (v != 0);
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	public int read() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		return this.readUnsignedByte();
	}
	
	/**
	 *	Reads an unsigned 8-bit number.
	 */
	public int readUnsignedByte() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			int ret = ByteTransformer.toUnsignedByte(dataToRead, meter);
			meter++;
			return ret;
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	/**
	 *	Reads an unsigned 16-bit number
	 */
	public int readUnsignedShort() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			int ret = ByteTransformer.toUnsignedShort(dataToRead, meter);
			meter += 2;
			return ret;
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	/**
	 *	Reads signed 16-bit number
	 */
	public short readShort() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		return (short)this.readUnsignedShort();
	}
	
	/**
	 *	Reads a Unicode character.
	 */
	public char readChar() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		return (char)this.readUnsignedShort();
	}
	
	/**
	 *	Reads a signed 32-bit integer.
	 */
	public int readInt() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			int ret = ByteTransformer.toInteger(dataToRead, meter);
			meter += 4;
			return ret;
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	/**
	 *	Reads a signed 64-bit integer.
	 */
	public long readLong() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			long ret = ByteTransformer.toLong(dataToRead, meter);
			meter += 8;
			return ret;
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	/**
	 *	Reads a <code>float</code>.
	 */
	public float readFloat() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			float ret = ByteTransformer.toFloat(dataToRead, meter);
			meter += 4;
			return ret;
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	/**
	 *	Reads a <code>double</code>.
	 */
	public double readDouble() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		try {
			double ret = ByteTransformer.toDouble(dataToRead, meter);
			meter += 8;
			return ret;
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfStreamException();
		}
	}
	
	/**
	 *	Reads in a string that has been encoded using a modified
	 *	UTF-8 format.
	 */
	public String readUTF() throws IOException {
		if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		int utflen = this.readUnsignedShort();
		char str[] = new char[utflen];
		int count = 0;
		int strlen = 0;
		
		while (count < utflen) {
			int c = this.readUnsignedByte();
			int char2, char3;
			switch (c >> 4) {
		        case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
				// 0xxxxxxx
					count++;
					str[strlen++] = (char)c;
					break;
				case 12: case 13:
				// 110x xxxx   10xx xxxx
					count += 2;
					if (count > utflen) throw new UTFDataFormatException();
					
					char2 = this.readUnsignedByte();
					if ((char2 & 0xC0) != 0x80) throw new UTFDataFormatException();
					
					str[strlen++] = (char)(((c & 0x1F) << 6) | (char2 & 0x3F));
					break;
				case 14:
				// 1110 xxxx  10xx xxxx  10xx xxxx
					count += 3;
					if (count > utflen) throw new UTFDataFormatException();
					
					char2 = this.readUnsignedByte();
					char3 = this.readUnsignedByte();
					if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80)) {
						throw new UTFDataFormatException();
					}
					
					str[strlen++] = (char)
							(((c & 0x0F) << 12) |
							 ((char2 & 0x3F) << 6) |
							 ((char3 & 0x3F) << 0));
					break;
				
				default:
				// 10xx xxxx,  1111 xxxx
					throw new UTFDataFormatException();
			}
		}
		
        return new String(str, 0, strlen);
    }
    
	/**
	 *	Reads the next line of text from this data input stream.
	 */
    public String readLine() throws IOException {
    	if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
    	BufferedReader in = new BufferedReader(new InputStreamReader(this));
    	return in.readLine();
    }
    
    public Object readObject() throws IOException, ClassNotFoundException {
    	if (this.closed) {
			throw new IOException("The stream is closed");
		}
		
		ObjectInputStream in = new ObjectInputStream(this);
		return in.readObject();
	}
	
    public byte[] toByteArray() {
    	int avail = this.available();
    	if (avail >= 0) {
			byte[] newbuf = new byte[avail];
			System.arraycopy(this.dataToRead, 0, newbuf, 0, avail);
			return newbuf;
		} else {
			return null;
		}
	}
	
}