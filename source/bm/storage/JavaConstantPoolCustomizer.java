/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.storage;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import douglas.mencken.beans.*;
import douglas.mencken.tools.DialogFactory;

/**
 *	<code>JavaConstantPoolCustomizer</code>
 *
 *	@version	0.6d0
 *	@since		Bytecode Maker 0.6.0
 */

public class JavaConstantPoolCustomizer {
	
	// ...
	
}



// ===========================================================================
//	JavaConstantPoolCustomizer.java (part of douglas.mencken.bm.frames package)
//		public class JavaConstantPoolCustomizer
//		class JavaConstantPoolCustomizerMenuBar
//		class ConstantPoolReferencesFrame
// ===========================================================================

/**
 *	<code>JavaConstantPoolCustomizer</code>
 *
 *	@version	1.25f1
 */

/*public class JavaConstantPoolCustomizer extends ClassMemberFrame
implements ActionListener, PropertyChangeListener {
	
	JavaClass theClass = null;
	List constantsList;
	private int currentIndex = -1;
	
	public JavaConstantPoolCustomizer(WindowListener listener) {
		super("Constant Pool");
		new JavaConstantPoolCustomizerMenuBar(this, listener);
		
		Dimension screenSize = super.getToolkit().getScreenSize();
		setSize(500, screenSize.height - 40);
		setResizable(false);
		
		this.constantsList = new List();
		constantsList.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		constantsList.addActionListener(this);
		
		add("Center", constantsList);
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
		constantsList.removeAll();
		
		if (theClass != null) {
			JavaConstantPoolElement[] constants = theClass.getPoolManager().getConstants();
			int len = constants.length;
			
			for (int i = 1; i < len; i++) {
				if (constants[i] != null) {
					addToConstantsList(constants[i].toString());
				}
			}
		}
	}*/
	
	/**
	 *	Adds a <code>String item</code> to the list of constants.
	 */
	/*void addToConstantsList(String item) {
		this.constantsList.add(item);
	}
	
	public void actionPerformed(ActionEvent evt) {
		this.currentIndex = constantsList.getSelectedIndex();
		
		String cmd = evt.getActionCommand();
		int constantIndex = Integer.parseInt(
			StringUtilities.getAfter(StringUtilities.getBefore(cmd, ' '), '#')
		);
		
		JavaConstantPoolElement constant = theClass.getPoolManager().getConstants()[constantIndex];
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
		constantsList.replaceItem((String)evt.getNewValue(), this.currentIndex);
		
		// update other constants
		// ...
	}
	
}


class JavaConstantPoolCustomizerMenuBar extends MenuBar implements ActionListener {
	
	private JavaConstantPoolCustomizer owner;
	private WindowListener listener;
	private ConstantPoolReferencesFrame refsFrame = null;
	
	JavaConstantPoolCustomizerMenuBar(JavaConstantPoolCustomizer frame, WindowListener listener) {
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
			String[] contstants = owner.constantsList.getItems();
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
					ConstantPoolManager.dumpPoolReferences(owner.theClass)
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
				ConstantPoolManager poolManager = owner.theClass.getPoolManager();
				if (!poolManager.addConstant(newJavaConstantPoolElement)) {
					UsefulMessageDialogs.doInfoDialog("No constant has been created.");
				} else {
					owner.addToConstantsList(newJavaConstantPoolElement.toString());
				}
			}
		} else if (c.equals("REMOVE")) {
			// ConstantPoolManager poolManager = owner.theClass.getPoolManager();
			// ...
			// owner.update();
		}
	}
	
}*/


/*class ConstantPoolReferencesFrame extends TextViewFrame {
	
	ConstantPoolReferencesFrame(String contents) {
		super("Constant Pool References", new TextView());
		
		super.getTextView().setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		super.getTextView().appendln(contents);
		super.setSize(super.getSize().width + 100, super.getSize().height - 30);
	}
	
}*/

// public class JavaConstantPoolElementCustomizer extends Panel
// implements Customizer, TextListener, ItemListener, ActionListener {
	
	//private PropertyChangeSupport propertyChangeSupport;
	//private JavaConstantPoolElement constant;
	
	//private Choice typeSelector;
	//private TextField contentsTF;
	//private TextField reference1TF;
	//private TextField reference2TF;
	
	//private LWLabel tagLabel;
	//private LWLabel contentsLabel;
	//private LWLabel referenceLabel1;
	//private LWLabel referenceLabel2;
	
	//private LWButton searchButton1;
	//private LWButton searchButton2;
	
	/**
	 *	(static method)
	 */
	/*public static Choice makeConstantTypeSelector() {
		Choice typeSelector = new Choice();
		
		int types_count = JavaConstantPool.CONSTANT_TYPES.length;
		for (int i = 1; i < types_count; i++) {
			typeSelector.add(JavaConstantPool.CONSTANT_TYPES[i]);
		}
		
		typeSelector.setFont(new Font("Geneva", Font.PLAIN, 10));
		typeSelector.setSize(100, 20);
		
		return typeSelector;
	}*/
	
	/**
	 *	Initializes a new <code>JavaConstantPoolElementCustomizer</code>.
	 */
	/*public JavaConstantPoolElementCustomizer() {
		super(null);
		
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.constant = new JavaConstantPoolElement();
		
		this.addComponents();
		
		super.setSize(this.getPreferredSize());
		this.update();
	}*/
	
	/*private void addComponents() {
		this.tagLabel = new LWLabel("Tag: ", LWLabel.LEFT);
		this.tagLabel.setLocation(5, 13);
		
		this.typeSelector = makeConstantTypeSelector();
		this.typeSelector.setLocation(95, 10);
		this.typeSelector.addItemListener(this);
		
		this.contentsLabel = new LWLabel("Contents: ", LWLabel.LEFT);
		this.contentsLabel.setLocation(5, 38);
		
		this.contentsTF = new TextField();
		this.contentsTF.setFont(new Font("Geneva", Font.PLAIN, 10));
		this.contentsTF.setBounds(95, 35, 300, 20);
		
		this.referenceLabel1 = new LWLabel("Reference 1: ", LWLabel.LEFT);
		this.referenceLabel1.setLocation(5, 63);
		
		this.reference1TF = new TextField();
		this.reference1TF.setFont(new Font("Geneva", Font.PLAIN, 10));
		this.reference1TF.setBounds(95, 60, 230, 20);
		
		this.searchButton1 = new LWButton("Search");
		this.searchButton1.setFont(new Font("Geneva", Font.BOLD, 9));
		this.searchButton1.setActionCommand("SEARCH_1");
		this.searchButton1.addActionListener(this);
		this.searchButton1.setLocation(340, 60);
		
		this.referenceLabel2 = new LWLabel("Reference 2: ", LWLabel.LEFT);
		this.referenceLabel2.setLocation(5, 88);
		
		this.reference2TF = new TextField();
		this.reference2TF.setFont(new Font("Geneva", Font.PLAIN, 10));
		this.reference2TF.setBounds(95, 85, 230, 20);
		
		this.searchButton2 = new LWButton("Search");
		this.searchButton2.setFont(new Font("Geneva", Font.BOLD, 9));
		this.searchButton2.setActionCommand("SEARCH_2");
		this.searchButton2.addActionListener(this);
		this.searchButton2.setLocation(340, 85);
		
		super.add(this.tagLabel);
		super.add(this.typeSelector);
		
		super.add(this.contentsLabel);
		super.add(this.contentsTF);
		super.add(this.referenceLabel1);
		super.add(this.reference1TF);
		super.add(this.searchButton1);
		super.add(this.referenceLabel2);
		super.add(this.reference2TF);
		super.add(this.searchButton2);
	}*/
	
	//public void update() {
	//	if (constant.isBasicType()) {
	//		this.referenceLabel1.setEnabled(false);
	//		this.reference1TF.setEnabled(false);
	//		this.reference1TF.removeTextListener(this);
	//		this.referenceLabel2.setEnabled(false);
	//		this.reference2TF.setEnabled(false);
	//		this.reference2TF.removeTextListener(this);
	//		
	//		this.searchButton1.setEnabled(false);
	//		this.searchButton2.setEnabled(false);
	//		
	//		this.contentsLabel.setEnabled(true);
	//		this.contentsTF.setEnabled(true);
	//		this.contentsTF.addTextListener(this);
	//		this.contentsTF.requestFocus();
	//	} else {
	//		int tag = constant.getTag();
	//		if ((tag == /* Classref */ 7) || (tag == /* Stringref */ 8)) {
	//			this.contentsLabel.setEnabled(false);
	//			this.contentsTF.setEnabled(false);
	//			this.contentsTF.removeTextListener(this);
	//			this.referenceLabel2.setEnabled(false);
	//			this.reference2TF.setEnabled(false);
	//			this.reference2TF.removeTextListener(this);
	//			
	//			this.searchButton1.setEnabled(true);
	//			this.searchButton2.setEnabled(false);
	//			
	//			this.referenceLabel1.setEnabled(true);
	//			this.reference1TF.setEnabled(true);
	//			this.reference1TF.addTextListener(this);
	//			this.reference1TF.requestFocus();
	//		} else {
	//			this.contentsLabel.setEnabled(false);
	//			this.contentsTF.setEnabled(false);
	//			this.contentsTF.removeTextListener(this);
	//			
	//			this.searchButton1.setEnabled(true);
	//			this.searchButton2.setEnabled(true);
	//			
	//			this.referenceLabel1.setEnabled(true);
	//			this.reference1TF.setEnabled(true);
	//			this.reference1TF.addTextListener(this);
	//			this.referenceLabel2.setEnabled(true);
	//			this.reference2TF.setEnabled(true);
	//			this.reference2TF.addTextListener(this);
	//			
	//			this.reference1TF.requestFocus();
	//		}
	//	}
	//}
	
	//public Dimension getPreferredSize() {
	//	return new Dimension(/* width */ 420, /* height */ 120);
	//}
	
	//public void addPropertyChangeListener(PropertyChangeListener pcl) {
	//	propertyChangeSupport.addPropertyChangeListener(pcl);
	//}
	
	//public void removePropertyChangeListener(PropertyChangeListener pcl) {
	//	propertyChangeSupport.removePropertyChangeListener(pcl);
	//}
	
	/*public void setObject(Object bean) {
		this.constant = (JavaConstantPoolElement)bean;
		int tag = this.constant.getTag();
		this.typeSelector.select(tag - 1);
		
		this.contentsTF.setText(this.constant.getContents());
		this.reference1TF.setText(String.valueOf(this.constant.getReference()));
		this.reference2TF.setText(String.valueOf(this.constant.getReference2()));
		
		this.update();
	}
	
	public void actionPerformed(ActionEvent evt) {
		String contents = DialogFactory.askForOneString("Contents:");
		if ((contents != null) && (contents != "")) {
			int reference = 0;
			ConstantPoolManager poolManager = this.constant.getOwner().getPoolManager();
			
			try {
				reference = poolManager.findConstantByContents(contents).getNumber();
			} catch (Exception exc) {
				super.getToolkit().beep();
				return;
			}
			
			String command = evt.getActionCommand();
			if (command.equals("SEARCH_1")) {
				this.reference1TF.setText(String.valueOf(reference));
			} else if (command.equals("SEARCH_2")) {
				this.reference2TF.setText(String.valueOf(reference));
			}
		}
	}
	
	public void textValueChanged(TextEvent e) {
		this.fireChange();
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (!this.typeSelector.getSelectedItem().startsWith("*")) {
			this.fireChange();
		} else {
			super.getToolkit().beep();
			this.typeSelector.select(0);
		}
	}*/
	
	//private synchronized void fireChange() {
	//	String oldConst = constant.toString();
	//	
	//	int oldTag = constant.getTag();
	//	int newTag = this.typeSelector.getSelectedIndex() + 1;
	//	if (oldTag != newTag) {
	//		constant.setTag(newTag);
	//		this.update();
	//		
	//		if ((oldTag <= 6) && (newTag > 6)) {
	//			this.reference1TF.setText(String.valueOf(constant.getReference()));
	//			this.reference2TF.setText(String.valueOf(constant.getReference2()));
	//		}
	//	}
	//	
	//	int ref1 = constant.getReference();
	//	int ref2 = constant.getReference2();
	//	
	//	switch (newTag) {
	//		case /* UTF8 */ 1:
	//			constant.setContents(contentsTF.getText());
	//			break;
	//		
	//		case /* Integer */ 3:
	//			int integer = 0;
	//			try {
	//				integer = Integer.parseInt(contentsTF.getText());
	//			} catch (NumberFormatException exc) {
	//				super.getToolkit().beep();
	//				this.contentsTF.setText(String.valueOf(integer));
	//			}
	//			
	//			constant.setContents(String.valueOf(integer));
	//			break;
	//		
	//		case /* Float */ 4:
	//			Float f = new Float(0.0f);
	//			try {
	//				f = Float.valueOf(contentsTF.getText());
	//			} catch (NumberFormatException exc) {
	//				super.getToolkit().beep();
	//				this.contentsTF.setText(f.toString());
	//			}
	//			
	//			constant.setContents(f.toString());
	//			break;
	//		
	//		case /* Long */ 5:
	//			Long value = new Long(0L);
	//			try {
	//				value = Long.valueOf(contentsTF.getText());
	//			} catch (NumberFormatException exc) {
	//				super.getToolkit().beep();
	//				this.contentsTF.setText(value.toString());
	//			}
	//			
	//			constant.setContents(value.toString());
	//			break;
	//		
	//		case /* Double */ 6:
	//			Double d = new Double(0.0d);
	//			try {
	//				d = Double.valueOf(contentsTF.getText());
	//			} catch (NumberFormatException exc) {
	//				super.getToolkit().beep();
	//				this.contentsTF.setText(d.toString());
	//			}
	//			
	//			constant.setContents(d.toString());
	//			break;
	//		
	//		case /* Classref */ 7:
	//		case /* Stringref */ 8:
	//		{
	//			if (reference1TF.getText().length() > 0) {
	//				try {
	//					constant.setReference(Integer.parseInt(reference1TF.getText()));
	//				} catch (NumberFormatException exc) {
	//					super.getToolkit().beep();
	//					this.reference1TF.setText(String.valueOf(ref1));
	//				}
	//			}
	//		}
	//			break;
	//		
	//		case /* Fieldref */ 9:
	//		case /* Methodref */ 10:
	//		case /* InterfaceMethodref */ 11:
	//		case /* NameAndType */ 12:
	//		{
	//			if (reference1TF.getText().length() > 0) {
	//				try {
	//					constant.setReference(Integer.parseInt(reference1TF.getText()));
	//				} catch (NumberFormatException exc) {
	//					super.getToolkit().beep();
	//					this.reference1TF.setText(String.valueOf(ref1));
	//				}
	//			}
	//			
	//			if (reference2TF.getText().length() > 0) {
	//				try {
	//					constant.setReference2(Integer.parseInt(reference2TF.getText()));
	//				} catch (NumberFormatException exc) {
	//					super.getToolkit().beep();
	//					this.reference2TF.setText(String.valueOf(ref2));
	//				}
	//			}
	//		}
	//			break;
	//		
	//		default:
	//			super.getToolkit().beep();
	//			this.typeSelector.select(0);
	//	}
	//	
	//	if (!constant.isBasicType()) {
	//		try {
	//			this.contentsTF.setText(constant.getContents());
	//		} catch (IllegalArgumentException exc) {
	//			super.getToolkit().beep();
	//			constant.setReference(ref1);
	//			constant.setReference2(ref2);
	//			
	//			this.reference1TF.setText(String.valueOf(ref1));
	//			this.reference2TF.setText(String.valueOf(ref2));
	//			this.contentsTF.setText(constant.getContents());
	//		}
	//	} else {
	//		this.reference1TF.setText(String.valueOf(constant.getReference()));
	//		this.reference2TF.setText(String.valueOf(constant.getReference2()));
	//	}
	//	
	//	String newConst = constant.toString();
	//	propertyChangeSupport.firePropertyChange("JavaConstantPoolElement", oldConst, newConst);
	//}
//	
//}
