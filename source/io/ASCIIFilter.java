// ===========================================================================
//	ASCIIFilter.java (part of douglas.mencken.io package)
// ===========================================================================

package douglas.mencken.io;

import java.io.*;
import java.util.Vector;

/**
 *	<code>ASCIIFilter</code>
 *
 *	@version 1.04f
 */

public class ASCIIFilter extends FilterOutputStream {
	
	public ASCIIFilter(OutputStream out) {
		super(out);
	}
	
	public void write(int b) throws IOException {
		if ((b >= 32) && (b <= 128)) out.write(b);
		if (b == 10) out.write(10);
		if (b == 13) out.write(13);
	}
	
	public void write(byte[] data) throws IOException {
		this.write(data, 0, data.length);
	}
	
	public void write(byte[] data, int off, int len) throws IOException {
		for (int i = off; i < len; i++) {
			byte b = data[off + i];
			if ((b >= 32) && (b <= 128)) out.write(b);
			if (b == 10) out.write(10);
			if (b == 13) out.write(13);
		}
	}
	
	public static byte[] getFiltered(byte[] data) {
		if (data == null) return null;
		if (data.length == 0) return new byte[0];
		
		int len = data.length;
		byte[] filtered = new byte[len];
		int pos = 0;
		
		for (int i = 0; i < len; i++) {
			if ((data[i] == 10) || (data[i] == 13)) {
				filtered[pos] = data[i];
				pos++;
			} else if ((data[i] >= 32) && (data[i] < 128)) {
				filtered[pos] = data[i];
				pos++;
			}
		}
		
		byte[] out = new byte[pos];
		System.arraycopy(filtered, 0, out, 0, pos);
		
		return out;
	}
	
}