// ===========================================================================
//	ClassSourceFrame.java (part of douglas.mencken.bm.frames package)
//		public class ClassSourceFrame
//		class ClassSourceFrameMenuBar
//	
// ===========================================================================

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.beans.TextView;
import douglas.mencken.beans.TextViewFrame;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.tools.TextViewPopupMenu;

import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.decompiler.ClassDecompiler;

/*
 *	<code>ClassSourceFrame</code>
 *
 *	@version 1.11f3
 */

public class ClassSourceFrame extends TextViewFrame {
	
	private JavaClass theClass = null;
	private TextViewPopupMenu popupMenu;
	
	public ClassSourceFrame(WindowListener listener) {
		super("Source", new TextView(), true);
		TextView view = super.getTextView();
		view.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		
		this.popupMenu = new TextViewPopupMenu(view, "UntitledClass.java", false);
		view.add(this.popupMenu);
		
		new ClassSourceFrameMenuBar(this, listener);
		
		super.setResizable(false);
	}
	
	public void setClass(JavaClass newClass) {
		if (this.theClass != newClass) {
			this.theClass = newClass;
			if (theClass != null) {
				super.setTitle(newClass.getClassName() + ": Source");
			} else {
				super.setTitle("Source");
			}
			
			this.updateContents();
		}
	}
	
	public void updateContents() {
		super.getTextView().clear();
		String className = "UntitledClass";
		
		if (theClass != null) {
			super.getTextView().append(
				(new ClassDecompiler(theClass)).getString()
			);
			className = theClass.getClassName();
		}
		
		this.popupMenu.setFilename(ClassUtilities.getClassName(className) + ".java");
		
		super.invalidate();
		super.doLayout();
		super.validate();
	}
	
}


class ClassSourceFrameMenuBar extends MenuBar implements ActionListener {
	
	private ClassSourceFrame owner;
	private WindowListener listener;
	
	ClassSourceFrameMenuBar(ClassSourceFrame frame, WindowListener listener) {
		Menu file = new Menu("File");
		MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		file.add(close);
		
		super.add(file);
		frame.setMenuBar(this);
		this.owner = frame;
		this.listener = listener;
	}
	
	public void actionPerformed(ActionEvent evt) {
		String c = evt.getActionCommand();
		if (c.equals("CLOSE")) {
			listener.windowClosing(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
		}
	}
	
}