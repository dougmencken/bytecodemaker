/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.io.*;
import douglas.mencken.beans.TextView;

/**
 *	<code>TextViewOutputStream</code>
 *
 *	@version 1.06f2
 */

public class TextViewOutputStream extends OutputStream {
	
	private boolean changed = false;
	private TextView textView;
	private StringBuffer buf;
	
	public TextViewOutputStream(TextView outTo) {
		super();
		
		this.textView = outTo;
		this.buf = new StringBuffer();
	}
	
	public void write(int b) {
		this.write(b, true);
	}
	
	public void write(int b, boolean setChangeFlag) {
		writeChar(b);
		if (setChangeFlag) this.changed = true;
	}
	
	protected void writeChar(int b) {
		switch (b) {
			case 13:
				buf.append('\n');
				break;
			case 9:
				buf.append("    ");
				break;
			
			default:
				buf.append((char)b);
				break;
		}
	}
	
	public void clear() {
		buf.setLength(0);
		textView.clear();
	}
	
	public void flush() {
		textView.append(buf.toString());
		buf.setLength(0);
		textView.invalidate();
	}
	
	public boolean isChanged() { return changed; }
	
	public void setChanged(boolean newChanged) {
		this.changed = newChanged;
		if (!newChanged) flush();
	}
	
}