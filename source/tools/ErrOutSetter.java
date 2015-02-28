// ===========================================================================
// BMOutSetter.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import douglas.mencken.beans.TextView;
import douglas.mencken.beans.TextViewOutputStream;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.exceptions.TooManyInstancesException;

/**
 *	<code>ErrOutSetter</code>
 *
 *	@version 1.03f1
 */

public class ErrOutSetter extends Object implements WindowListener, Runnable {
	
	private Thread engine;
	private TextViewOutputStream out;
	private TextViewOutputStream err;
	private ErrOutFrame outFrame;
	private ErrOutFrame errFrame;
	
	private static ErrOutSetter outSetter = null;
	
	public static void disposeFrames() {
		if (outSetter != null) {
			outSetter.outFrame.dispose();
			outSetter.errFrame.dispose();
		}
	}
	
	public ErrOutSetter() throws TooManyInstancesException {
		super();
		if (outSetter != null) {
			throw new TooManyInstancesException(super.getClass().getName());
		}
		
		outSetter = this;
		
		TextView outPanel = new TextView();
		outPanel.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		TextView errPanel = new TextView();
		errPanel.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		
		this.out = new TextViewOutputStream(outPanel);
		this.err = new TextViewOutputStream(errPanel);
		System.setOut(new PrintStream(out));	// new PrintWriter(out, true);
		System.setErr(new PrintStream(err));	// new PrintWriter(err, true);
		
		this.outFrame = new ErrOutFrame("out", outPanel);
		outFrame.addWindowListener(this);
		this.errFrame = new ErrOutFrame("err", errPanel);
		errFrame.addWindowListener(this);
		
		this.engine = new Thread(this);
		engine.setDaemon(true);
		engine.start();
	}
	
	// public OutputStream getOutStream() { return this.out; }
	// public OutputStream getErrStream() { return this.err; }
	
	public void run() {
		while (true) {
			if (out.isChanged()) {
				out.setChanged(false);
				outFrame.setVisible(true);
			}
			
			if (err.isChanged()) {
				err.setChanged(false);
				errFrame.setVisible(true);
			}
		}
	}
	
	public void windowClosing(WindowEvent e) {
		Window src = (Window) e.getSource();
		if (src == outFrame) {
			outFrame.setVisible(false);
			out.clear();
		}
		
		if (src == errFrame) {
			errFrame.setVisible(false);
			err.clear();
		}
	}
	
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	
}
