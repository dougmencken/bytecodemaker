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
 *	class <code>JavaConstantPoolElementEditor</code>
 */

public class JavaConstantPoolElementEditor extends Panel
implements Customizer, TextListener, ItemListener, ActionListener {

	private PropertyChangeSupport propertyChangeSupport;
	private JavaConstantPoolElement constant;
	
	private Choice typeSelector;
	private TextField contentsTF;
	private TextField reference1TF;
	private TextField reference2TF;
	
	private LWLabel tagLabel;
	private LWLabel contentsLabel;
	private LWLabel referenceLabel1;
	private LWLabel referenceLabel2;
	
	private LWButton searchButton1;
	private LWButton searchButton2;
	
	/**
	 *	(static method)
	 */
	public static Choice makeConstantTypeSelector() {
		Choice typeSelector = new Choice();
		
		int types_count = JavaConstantPool.CONSTANT_TAGS.length;
		for (int i = 1; i < types_count; i++) {
			typeSelector.add(JavaConstantPool.CONSTANT_TAGS[i]);
		}
		
		typeSelector.setFont(new Font("Geneva", Font.PLAIN, 10));
		typeSelector.setSize(100, 20);
		
		return typeSelector;
	}
	
	/**
	 *	Initializes a new <code>JavaConstantPoolElementEditor</code>.
	 */
	public JavaConstantPoolElementEditor() {
		super(null);
		
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.constant = new JavaConstantPoolElement();
		
		this.addComponents();
		
		super.setSize(this.getPreferredSize());
/////		this.update();
	}
	
	private void addComponents() {
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
	}
	
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
	
	public Dimension getPreferredSize() {
		return new Dimension(/* width */ 420, /* height */ 120);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.addPropertyChangeListener(pcl);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.removePropertyChangeListener(pcl);
	}
	
	public void setObject(Object bean) {
		this.constant = (JavaConstantPoolElement)bean;
		int tag = this.constant.getTag();
		this.typeSelector.select(tag - 1);
		
		this.contentsTF.setText(this.constant.getContents());
		this.reference1TF.setText(String.valueOf(this.constant.getReference()));
		this.reference2TF.setText(String.valueOf(this.constant.getReference2()));
		
//////		this.update();
	}
	
	public void actionPerformed(ActionEvent evt) {
		String contents = DialogFactory.askForOneString("Contents:");
		if ((contents != null) && (contents != "")) {
			int reference = 0;
	///////		JavaConstantPool pool = this.constant.getPool();
			
	/////		try {
	////			reference = pool.findConstantByContents(contents).getNumber();
	//////		} catch (Exception exc) {
	///////			super.getToolkit().beep();
	////			return;
	/////		}
			
			String command = evt.getActionCommand();
			if (command.equals("SEARCH_1")) {
	////			this.reference1TF.setText(String.valueOf(reference));
			} else if (command.equals("SEARCH_2")) {
	///////			this.reference2TF.setText(String.valueOf(reference));
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
	}
	
	private synchronized void fireChange() {
		String oldConst = this.constant.toString();
		
		int oldTag = constant.getTag();
		int newTag = this.typeSelector.getSelectedIndex() + 1;
		if (oldTag != newTag) {
///////			constant.setTag(newTag);
/////			this.update();
			
/////			if ((oldTag <= 6) && (newTag > 6)) {
//////				this.reference1TF.setText(String.valueOf(constant.getReference()));
/////				this.reference2TF.setText(String.valueOf(constant.getReference2()));
///////			}
		}
		
		int ref1 = constant.getReference();
		int ref2 = constant.getReference2();
		
		switch (newTag) {
			case /* UTF8 */ 1:
				constant.setContents(contentsTF.getText());
				break;
			
			case /* Integer */ 3:
				int integer = 0;
				try {
					integer = Integer.parseInt(contentsTF.getText());
				} catch (NumberFormatException exc) {
					super.getToolkit().beep();
					this.contentsTF.setText(String.valueOf(integer));
				}
				
				constant.setContents(String.valueOf(integer));
				break;
			
			case /* Float */ 4:
				Float f = new Float(0.0f);
				try {
					f = Float.valueOf(contentsTF.getText());
				} catch (NumberFormatException exc) {
					super.getToolkit().beep();
					this.contentsTF.setText(f.toString());
				}
				
				constant.setContents(f.toString());
				break;
			
			case /* Long */ 5:
				Long value = new Long(0L);
				try {
					value = Long.valueOf(contentsTF.getText());
				} catch (NumberFormatException exc) {
					super.getToolkit().beep();
					this.contentsTF.setText(value.toString());
				}
				
				constant.setContents(value.toString());
				break;
			
			case /* Double */ 6:
				Double d = new Double(0.0d);
				try {
					d = Double.valueOf(contentsTF.getText());
				} catch (NumberFormatException exc) {
					super.getToolkit().beep();
					this.contentsTF.setText(d.toString());
				}
				
				constant.setContents(d.toString());
				break;
			
			case /* Classref */ 7:
			case /* Stringref */ 8:
			{
				if (reference1TF.getText().length() > 0) {
					try {
						constant.setReference(Integer.parseInt(reference1TF.getText()));
					} catch (NumberFormatException exc) {
						super.getToolkit().beep();
						this.reference1TF.setText(String.valueOf(ref1));
					}
				}
			}
				break;
			
			case /* Fieldref */ 9:
			case /* Methodref */ 10:
			case /* InterfaceMethodref */ 11:
			case /* NameAndType */ 12:
			{
				if (reference1TF.getText().length() > 0) {
					try {
						constant.setReference(Integer.parseInt(reference1TF.getText()));
					} catch (NumberFormatException exc) {
						super.getToolkit().beep();
						this.reference1TF.setText(String.valueOf(ref1));
					}
				}
				
				if (reference2TF.getText().length() > 0) {
					try {
						constant.setReference2(Integer.parseInt(reference2TF.getText()));
					} catch (NumberFormatException exc) {
						super.getToolkit().beep();
						this.reference2TF.setText(String.valueOf(ref2));
					}
				}
			}
				break;
			
			default:
				super.getToolkit().beep();
				this.typeSelector.select(0);
		}
		
		if (!constant.isBasicType()) {
			try {
				this.contentsTF.setText(constant.getContents());
			} catch (IllegalArgumentException exc) {
				super.getToolkit().beep();
				constant.setReference(ref1);
				constant.setReference2(ref2);
				
				this.reference1TF.setText(String.valueOf(ref1));
				this.reference2TF.setText(String.valueOf(ref2));
				this.contentsTF.setText(constant.getContents());
			}
		} else {
			this.reference1TF.setText(String.valueOf(constant.getReference()));
			this.reference2TF.setText(String.valueOf(constant.getReference2()));
		}
		
		String newConst = constant.toString();
		propertyChangeSupport.firePropertyChange("JavaConstantPoolElement", oldConst, newConst);
	}

}
