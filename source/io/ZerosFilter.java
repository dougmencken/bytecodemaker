// ===========================================================================
//	ZerosFilter.java (part of douglas.mencken.io package)
// ===========================================================================

package douglas.mencken.io;

import java.io.*;
import java.util.Vector;

/**
 *	<code>ZerosFilter</code>
 *	(final class)
 *
 *	@version 1.01f2
 */

public final class ZerosFilter extends FilterOutputStream {

	public ZerosFilter(OutputStream out) {
		super(out);
	}
	
	public void write(int b) throws IOException {
		if (b != 0) out.write(b);
	}
	
	public void write(byte[] data, int off, int len) throws IOException {
		for (int i = 0; i < len; i++) {
			byte b = data[off + i];
			if (b != 0) out.write(b);
		}
	}
	
	public static byte[] getFiltered(byte[] data) {
		int len = data.length;
		Vector vector = new Vector(len);
		
		for (int i = 0; i <len; i++) {
			if (data[i] != 0) vector.addElement(new Byte(data[i]));
		}
		
		int vectorSize = vector.size();
		byte[] out = new byte[vectorSize];
		
		for (int i = 0; i < vectorSize; i++) {
			out[i] = ((Byte)vector.elementAt(i)).byteValue();
		}
		
		return out;
	}
	
}