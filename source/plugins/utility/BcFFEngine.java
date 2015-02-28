// ===========================================================================
//	BcFFEngine.java
// ===========================================================================

package douglas.mencken.bm.plugins;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import douglas.mencken.io.EndOfStreamException;
import douglas.mencken.tools.UsefulModalDialogs;

/**
 *	<code>BcFFEngine</code><br>
 *	An utility class for BcFFCoder.
 *
 *	@version 1.11v
 */

public class BcFFEngine extends Object {
	
	protected InputStream in;
	protected OutputStream out;
	private boolean bcff = false;
	
	public BcFFEngine(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
		
		try {
			this.encode();
		} catch (EndOfStreamException eos) {
		} catch (IOException ioe) {
			UsefulModalDialogs.doErrorDialog("IOException caught in BcFFEngine");
		}
	}
	
	private void encode() throws IOException {
		int[] header = new int[4];
		header[0] = in.read();
		header[1] = in.read();
		header[2] = in.read();
		header[3] = in.read();
		
		if ((header[0] == 0xBC) &&
			 (header[1] == 0xFF) &&
			 (header[2] == 0x00) &&
			 (header[3] == 0x00)) {
			bcff = true;
		} else if ((header[0] == 0xCA) &&
					(header[1] == 0xFE) &&
					(header[2] == 0xBA) &&
					(header[3] == 0xBE)) {
			bcff = false;
			
			out.write(0xCA);
			out.write(0xFE);
			out.write(0xBA);
			out.write(0xBE);
			
			int current = in.read();
			while (current != -1) {
				out.write(current);
				current = in.read();
			}
			
			return;
		} else {
			bcff = false;
			
			out.write(0xBC);
			out.write(0xFF);
			out.write(0x00);
			out.write(0x00);
			
			for (int i = 0; i < 4; i++) {
				out.write(byteToBcFF(header[i]));
			}
		}
		
		this.convertToBcFF();
	}
	
	protected void convertToBcFF() throws IOException {
		int current = in.read();
		while (current != -1) {
			out.write(byteToBcFF(current));
			current = in.read();
		}
	}
	
	public boolean isBcFF() { return bcff; }
	
	public static byte byteToBcFF(int in) {
		if (in == 32) {
			return 13;
		} else if (in == 13) {
			return 32;
		} else {
			return (byte)(in - 128);
		}
	}
	
}