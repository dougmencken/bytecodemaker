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

import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.beans.support.*;

import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.storage.JavaField;

/**
 *	<code>FieldsFrame</code>
 *
 *	@version	1.12f3
 */

public class FieldsFrame extends Frame
implements ActionListener, PropertyChangeListener {
	
	JavaClass theClass = null;
	private List fieldsList;
	private transient int currentIndex = -1;
	
	public FieldsFrame(WindowListener listener) {
		super("Fields");
		new FieldsFrameMenuBar(this, listener);
		
		setSize(380, 300);
		setResizable(false);
		
		this.fieldsList = new List();
		fieldsList.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		fieldsList.addActionListener(this);
		
		add("Center", fieldsList);
	}
	
	public void setClass(JavaClass newClass) {
		if (this.theClass == newClass) return;
		
		this.theClass = newClass;
		if (theClass != null) {
			setTitle(newClass.getClassName() + ": Fields");
		} else {
			setTitle("Fields");
		}
		
		this.updateContents();
		((FieldsFrameMenuBar)super.getMenuBar()).updateMenu();
	}
	
	public void updateContents() {
		fieldsList.removeAll();
		
		if (theClass != null) {
			JavaField[] fields = theClass.getFields();
			
			if ((fields != null) && (fields.length != 0)) {
				int len = fields.length;
				for (int i = 0; i < len; i++) {
					fieldsList.add(fields[i].toString());
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if (cmd.startsWith("@FIELD")) {
			this.currentIndex = Integer.parseInt(cmd.substring(6));
		} else {
			this.currentIndex = fieldsList.getSelectedIndex();
		}
		
		JavaField field = theClass.getFields()[currentIndex];
		
		try {
			String description = "field description"; //**//**//**//* WAS field.getType() + ' ' + field.getFieldName();
			CustomizerFrame frame = CustomizerFrame.customizeBean(
				field, this, description, this
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
		fieldsList.replaceItem((String)evt.getNewValue(), this.currentIndex);
		((FieldsFrameMenuBar)super.getMenuBar()).updateMenu();
	}
	
}


class FieldsFrameMenuBar extends MenuBar implements ActionListener {
	
	private FieldsFrame owner;
	private WindowListener listener;
	private Menu fieldsMenu;
	private MenuItem removeField;
	
	FieldsFrameMenuBar(FieldsFrame frame, WindowListener listener) {
		MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		
		Menu file = new Menu("File");
		file.add(close);
		super.add(file);
		
		MenuItem addField = new MenuItem("Add Field...");
		addField.setActionCommand("ADD");
		addField.addActionListener(this);
		this.removeField = new MenuItem("Remove Field...");
		this.removeField.setActionCommand("REMOVE");
		this.removeField.addActionListener(this);
		
		this.fieldsMenu = new Menu("Fields");
		this.fieldsMenu.add(addField);
		this.fieldsMenu.add(removeField);
		
		super.add(fieldsMenu);
		frame.setMenuBar(this);
		this.owner = frame;
		this.listener = listener;
		
		this.updateMenu();
	}
	
	void updateMenu() {
		synchronized (super.getTreeLock()) {
			this.removeField.setEnabled(false);
			
			int itemCount = fieldsMenu.getItemCount() - 2;
			for (int i = 0; i < itemCount; i++) {
				fieldsMenu.remove(2);
		    }
		    
			if (this.owner.theClass != null) {
				fieldsMenu.addSeparator();
				
//////////////				JavaField[] allFields = this.owner.theClass.getFields();
//////////////				int count = allFields.length;
				int count = 0;
				
				if (count > 0) {
					for (int i = 0; i < count; i++) {
//////////						MenuItem currentField = new MenuItem(allFields[i].getFieldName());
//////////						currentField.setActionCommand("@FIELD" + i);
//////////						currentField.addActionListener(this.owner);
						
//////////						fieldsMenu.add(currentField);
					}
					
					this.removeField.setEnabled(true);
				} else {
					MenuItem noFields = new MenuItem("(no fields)");
					fieldsMenu.add(noFields);
					noFields.setEnabled(false);
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("CLOSE")) {
			listener.windowClosing(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
		} else if (cmd.equals("ADD")) {
			NewBeanCustomizerDialog newDialog = new NewBeanCustomizerDialog(
				"Field", JavaField.class
			);
			newDialog.setVisible(true);
			
			JavaField newJavaField = (JavaField)newDialog.getBean();
			if (newJavaField != null) {
				owner.theClass.addField(newJavaField);
				
				this.updateMenu();
				owner.updateContents();
				
				try {
					ClassFrame.getCurrentFrame().updateContents();
				} catch (Exception exc) { /* ... */ }
			}
		} else if (cmd.equals("REMOVE")) {
			// ... show list of fields with checkboxes ... //
///&&&&			owner.theClass.removeField(0);
			
			this.updateMenu();
			owner.updateContents();
			
			try {
				ClassFrame.getCurrentFrame().updateContents();
			} catch (Exception exc) { /* ... */ }
		}
	}
	
}
