/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;

import douglas.mencken.bm.storage.*;
import douglas.mencken.bm.frames.*;
import douglas.mencken.bm.engine.ConstantPoolLocksmith;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.io.AutoTypeCreatorFixer;
import douglas.mencken.io.SaveDialog;
import douglas.mencken.util.*;
import douglas.mencken.beans.*;
import douglas.mencken.beans.support.*;

/**
 *	<code>JavaConstantPoolFrame</code>
 *
 *	@version	1.2
 *	@since		Bytecode Maker A.6
 */

public class JavaConstantPoolFrame extends Frame
implements ActionListener, PropertyChangeListener {

	JavaClass theClass = null;
	List listOfConstants;
	private int currentIndex = -1;
	
	public JavaConstantPoolFrame(WindowListener listener) {
		super("Constant Pool");
		new JavaConstantPoolFrameMenuBar(this, listener);
		
		Dimension screenSize = super.getToolkit().getScreenSize();
		setSize(500, screenSize.height - 40);
		setResizable(false);
		
		this.listOfConstants = new List();
		listOfConstants.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		listOfConstants.addActionListener(this);
		
		add("Center", listOfConstants);
	}
	
	public void setClass(JavaClass newClass) {
		if (this.theClass == newClass) return;
		
		this.theClass = newClass;
		if (newClass != null) {
			super.setTitle(newClass.getClassName() + ": Constant Pool");
		} else {
			super.setTitle("Constant Pool");
		}
		
		this.updateContents();
	}
	
	public void updateContents() {
		listOfConstants.removeAll();
		
		if (theClass != null) {
			JavaConstantPoolElement[] constants = theClass.getConstantPool().getConstants();
			int len = constants.length;
			
			for (int i = 1; i < len; i++) {
				if (constants[i] != null) {
					addToListOfConstants(constants[i].toString());
				}
			}
		}
	}
	
	/**
	 *	Adds a <code>String item</code> to the list of constants.
	 */
	void addToListOfConstants(String item) {
		this.listOfConstants.add(item);
	}
	
	public void actionPerformed(ActionEvent evt) {
		this.currentIndex = this.listOfConstants.getSelectedIndex();
		
		String cmd = evt.getActionCommand();
		int constantIndex = Integer.parseInt(
			StringUtilities.getAfter(StringUtilities.getBefore(cmd, ' '), '#')
		);
		
		JavaConstantPoolElement constant = theClass.getConstantPool().getConstants()[constantIndex];
		try {
			CustomizerFrame frame = CustomizerFrame.customizeBean(
				(JavaConstantPoolElement)constant, this, constant.toString(false), this
			);
			
			frame.setResizable(false);
			frame.setVisible(true);
		} catch (Exception exc) {
			UsefulMessageDialogs.sayAboutInternalError(
				exc.getClass().getName() + ": " + exc.getMessage()
			);
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		// update constant
/////		this.listOfConstants.replaceItem((String)evt.getNewValue(), this.currentIndex);
		
		// update other constants
		// ...
	}
	
}


class JavaConstantPoolFrameMenuBar extends MenuBar implements ActionListener {
	
	private JavaConstantPoolFrame owner;
	private WindowListener listener;
	private ConstantPoolReferencesFrame refsFrame = null;
	
	JavaConstantPoolFrameMenuBar(JavaConstantPoolFrame frame, WindowListener listener) {
		MenuItem save = new MenuItem("Save...", new MenuShortcut(KeyEvent.VK_S));
		save.setActionCommand("SAVE");
		save.addActionListener(this);
		MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		
		Menu file = new Menu("File");
		file.add(save);
		file.add(close);
		super.add(file);
		
		MenuItem viewHierarchy = new MenuItem(
			"View Hierarchy", new MenuShortcut(KeyEvent.VK_H)
		);
		viewHierarchy.setActionCommand("HIERARCHY");
		viewHierarchy.addActionListener(this);
		MenuItem showReferences = new MenuItem(
			"Show References", new MenuShortcut(KeyEvent.VK_E)
		);
		showReferences.setActionCommand("SHOW_REF");
		showReferences.addActionListener(this);
		
		Menu options = new Menu("Options");
		options.add(viewHierarchy);
		options.add(showReferences);
		super.add(options);
		
		MenuItem addConstant = new MenuItem("Add Constant...");
		addConstant.setActionCommand("ADD");
		addConstant.addActionListener(this);
		MenuItem removeConstant = new MenuItem("Remove Constant...");
		removeConstant.setActionCommand("REMOVE");
		removeConstant.addActionListener(this);
		
		Menu constants = new Menu("Constants");
		constants.add(addConstant);
		constants.add(removeConstant);
		super.add(constants);
		
		frame.setMenuBar(this);
		this.owner = frame;
		this.listener = listener;
	}
	
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		if (c.equals("SAVE")) {
			String[] contstants = owner.listOfConstants.getItems();
			String out = StringUtilities.stringArrayToString(contstants, "\n");
			
			if (out != null) {
				String path = SaveDialog.showSaveFileDialog(
					ClassUtilities.getClassName(owner.theClass.getClassName()) + ".pool"
				);
				if (path != null) {
					AutoTypeCreatorFixer.getCurrentFixer().add(
						new File(path), "TEXT", "ByTe"
					);
					FileUtilities.writeBytesToFile(out.getBytes(), path);
				}
			}
		} else if (c.equals("CLOSE")) {
			if (refsFrame != null) {
				refsFrame.setVisible(false);
			}
			listener.windowClosing(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
		} else if (c.equals("HIERARCHY")) {
			Toolkit.getDefaultToolkit().beep();
		} else if (c.equals("SHOW_REF")) {
			if (refsFrame == null) {
				refsFrame = new ConstantPoolReferencesFrame(
					ConstantPoolLocksmith.dumpPoolReferences(owner.theClass)
				);
			}
			
			refsFrame.setVisible(true);
		} else if (c.equals("ADD")) {
			NewBeanCustomizerDialog newDialog = new NewBeanCustomizerDialog(
				"Constant", JavaConstantPoolElement.class
			);
			newDialog.setVisible(true);
			
			JavaConstantPoolElement newJavaConstantPoolElement = (JavaConstantPoolElement)newDialog.getBean();
			if (newJavaConstantPoolElement != null) {
				// add constant
				JavaConstantPool pool = owner.theClass.getConstantPool();
				owner.addToListOfConstants(newJavaConstantPoolElement.toString());
			}
		} else if (c.equals("REMOVE")) {
			// JavaConstantPool pool = owner.theClass.getConstantPool();
			// ...
			// owner.update();
		}
	}

}


class ConstantPoolReferencesFrame extends TextViewFrame {
	
	ConstantPoolReferencesFrame(String contents) {
		super("Constant Pool References", new TextView());
		
		super.getTextView().setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		super.getTextView().appendln(contents);
		super.setSize(super.getSize().width + 100, super.getSize().height - 30);
	}
	
}
