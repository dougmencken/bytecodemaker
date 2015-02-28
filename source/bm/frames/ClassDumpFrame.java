// ===========================================================================
// ClassDumpFrame.java
//	public class ClassDumpFrame
//	class ClassDumpFrameMenuBar
// ===========================================================================

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.beans.TextView;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.util.event.*;
import douglas.mencken.exceptions.*;
import douglas.mencken.tools.TextViewPopupMenu;

import douglas.mencken.bm.storage.*;
import douglas.mencken.bm.decompiler.MethodDecompiler;
import douglas.mencken.bm.decompiler.DecompilerUtilities;

/**
 *	<code>ClassDumpFrame</code>
 *
 *	@version 1.04f
 */

public class ClassDumpFrame extends Frame {
	
	private JavaClass theClass = null;
	private TextView outView;
	private TextViewPopupMenu popupMenu;
	
	public ClassDumpFrame(WindowListener listener) {
		super("Class Dump");
		new ClassDumpFrameMenuBar(this, listener);
		
		this.outView = new TextView();
		this.outView.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		this.popupMenu = new TextViewPopupMenu(outView, "UntitledClass.dump", false);
		this.outView.add(this.popupMenu);
		super.add(this.outView);
		
		Dimension screenSize = super.getToolkit().getScreenSize();
		super.setSize((int)(screenSize.width / 1.25f), screenSize.height - 20);
		super.setResizable(false);
	}
	
	public void setClass(JavaClass newClass) {
		if (this.theClass != newClass) {
			this.theClass = newClass;
			this.updateContents();
		}
	}
	
	public void updateContents() {
		if (this.theClass == null) {
			throw new InternalError("ClassDumpFrame: theClass = null");
		}
		
		// validate ????? the class
		// this.theClass.validate();
		
		this.outView.clear();
		boolean prepare = ((ClassDumpFrameMenuBar)super.getMenuBar()).dumpPrepared();
		
		String className = "UntitledClass";
		String suffix = (prepare) ? "pdump" : "dump";
		String title = "Dump";
		if (theClass != null) {
			className = this.theClass.getClassName();
			title = className + ": Class Dump";
			if (prepare) {
				title += " (Prepared Bytecodes)";
			}
		}
		
		super.setTitle(title);
		
		this.outView.appendln(
			StringUtilities.detab(DecompilerUtilities.dumpClass(theClass, prepare), 4)
		);
		this.popupMenu.setFilename(ClassUtilities.getClassName(className) + '.' + suffix);
		
		super.invalidate();
		super.doLayout();
		super.validate();
	}
	
}


class ClassDumpFrameMenuBar extends MenuBar implements ActionListener {
	
	private ClassDumpFrame owner;
	private WindowListener listener;
	private boolean dumpPrepared = false;
	
	ClassDumpFrameMenuBar(ClassDumpFrame frame, WindowListener listener) {
		Menu file = new Menu("File");
		MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		file.add(close);
		
		Menu options = new Menu("Options");
		CheckboxMenuItem prepareBytecodes = new CheckboxMenuItem("Dump Prepared Bytecodes");
		prepareBytecodes.setActionCommand("DUMP_PREPARED");
		prepareBytecodes.addActionListener(this);
		options.add(prepareBytecodes);
		
		super.add(file);
		super.add(options);
		
		frame.setMenuBar(this);
		this.owner = frame;
		this.listener = listener;
	}
	
	public void actionPerformed(ActionEvent evt) {
		String c = evt.getActionCommand();
		if (c.equals("CLOSE")) {
			listener.windowClosing(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
		} else if (c.equals("DUMP_PREPARED")) {
			this.dumpPrepared = !this.dumpPrepared;
			this.owner.updateContents();
		}
	}
	
	boolean dumpPrepared() {
		return this.dumpPrepared;
	}
	
}
