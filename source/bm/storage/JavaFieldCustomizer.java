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
import java.lang.reflect.Modifier;

import douglas.mencken.util.FontUtilities;
import douglas.mencken.beans.*;

/**
 *	@version	1.0a0
 *	@since		Bytecode Maker 0.5.8
 */

public class JavaFieldCustomizer extends Panel
implements Customizer, TextListener, ItemListener {
	
	private PropertyChangeSupport propertyChangeSupport;
	private JavaField field;
	
	private LWCheckbox publicAccessCheckbox;
	private LWCheckbox protectedAccessCheckbox;
	private LWCheckbox privateAccessCheckbox;
	private LWCheckbox defaultAccessCheckbox;
	private LWCheckbox staticAccessCheckbox;
	private LWCheckbox finalAccessCheckbox;
	private LWCheckbox transientAccessCheckbox;
	private LWCheckbox volatileAccessCheckbox;
	
	private TextField fieldType;
	private TextField fieldName;
	private LWLabel typeRefLabel;
	private LWLabel nameRefLabel;
	
	/**
	 *	Initializes a new <code>JavaFieldCustomizer</code>.
	 */
	public JavaFieldCustomizer() {
		super(null);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		// access checkboxes
		LWCheckboxGroup group = new LWCheckboxGroup();
		publicAccessCheckbox = new LWCheckbox("public", group, true);
		publicAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		publicAccessCheckbox.addItemListener(this);
		
		protectedAccessCheckbox = new LWCheckbox("protected", group, true);
		protectedAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		protectedAccessCheckbox.addItemListener(this);
		
		privateAccessCheckbox = new LWCheckbox("private", group, true);
		privateAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		privateAccessCheckbox.addItemListener(this);
		
		defaultAccessCheckbox = new LWCheckbox("default", group, false);
		defaultAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		defaultAccessCheckbox.addItemListener(this);
		
		staticAccessCheckbox = new LWCheckbox("static", false);
		staticAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		staticAccessCheckbox.addItemListener(this);
		
		finalAccessCheckbox = new LWCheckbox("final", false);
		finalAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		finalAccessCheckbox.addItemListener(this);
		
		transientAccessCheckbox = new LWCheckbox("transient", true);
		transientAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		transientAccessCheckbox.addItemListener(this);
		
		volatileAccessCheckbox = new LWCheckbox("volatile");
		volatileAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		volatileAccessCheckbox.addItemListener(this);
		
		this.fieldType = new TextField();
		this.fieldType.setFont(new Font("Geneva", Font.PLAIN, 10));
		this.fieldType.addTextListener(this);
		
		this.fieldName = new TextField();
		this.fieldName.setFont(new Font("Geneva", Font.PLAIN, 10));
		this.fieldName.addTextListener(this);
		
		this.publicAccessCheckbox.setBounds(70, 20, 100, 20);
		this.protectedAccessCheckbox.setBounds(70, 40, 100, 20);
		this.privateAccessCheckbox.setBounds(70, 60, 100, 20);
		this.defaultAccessCheckbox.setBounds(70, 80, 100, 20);
		this.staticAccessCheckbox.setBounds(200, 20, 100, 20);
		this.finalAccessCheckbox.setBounds(200, 40, 100, 20);
		this.transientAccessCheckbox.setBounds(200, 60, 100, 20);
		this.volatileAccessCheckbox.setBounds(200, 80, 100, 20);
		
		fieldType.setBounds(70, 105, 250, 20);
		fieldName.setBounds(70, 130, 250, 20);
		
		LWLabel accessLabel = new LWLabel("Access: ", LWLabel.LEFT);
		LWLabel typeLabel = new LWLabel("Type: ", LWLabel.LEFT);
		LWLabel nameLabel = new LWLabel("Name: ", LWLabel.LEFT);
		accessLabel.setLocation(5, 20);
		typeLabel.setLocation(5, 105);
		nameLabel.setLocation(5, 130);
		
		this.typeRefLabel = new LWLabel("@-1", LWLabel.LEFT);
		this.nameRefLabel = new LWLabel("@-1", LWLabel.LEFT);
		this.typeRefLabel.setForeground(Color.gray);
		this.nameRefLabel.setForeground(Color.gray);
		this.typeRefLabel.setLocation(330, 107);
		this.nameRefLabel.setLocation(330, 132);
		
		// 'final' fields contains "ConstantValue" attribute
		// ...
		
		super.add(accessLabel);
		super.add(typeLabel);
		super.add(nameLabel);
		
		super.add(this.publicAccessCheckbox);
		super.add(this.protectedAccessCheckbox);
		super.add(this.privateAccessCheckbox);
		super.add(this.defaultAccessCheckbox);
		super.add(this.staticAccessCheckbox);
		super.add(this.finalAccessCheckbox);
		super.add(this.transientAccessCheckbox);
		super.add(this.volatileAccessCheckbox);
		
		super.add(this.fieldType);
		super.add(this.fieldName);
		super.add(this.typeRefLabel);
		super.add(this.nameRefLabel);
		
		super.setSize(this.getPreferredSize());
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(/* width */ 370, /* height */ 180);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.addPropertyChangeListener(pcl);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.removePropertyChangeListener(pcl);
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.acceptChange();
	}
	
	public void textValueChanged(TextEvent e) {
		this.acceptChange();
	}
	
	public void setObject(Object bean) {
		this.field = (JavaField)bean;
		
		int access = this.field.getAccessModifiers();
		if (Modifier.isPublic(access)) {
			this.publicAccessCheckbox.setState(true);
		} else if (Modifier.isProtected(access)) {
			this.protectedAccessCheckbox.setState(true);
		} else if (Modifier.isPrivate(access)) {
			this.privateAccessCheckbox.setState(true);
		} else {
			this.defaultAccessCheckbox.setState(true);
		}
		
		this.staticAccessCheckbox.setState(Modifier.isStatic(access));
		this.finalAccessCheckbox.setState(Modifier.isFinal(access));
		this.transientAccessCheckbox.setState(Modifier.isTransient(access));
		this.volatileAccessCheckbox.setState(Modifier.isVolatile(access));
		
		this.fieldType.setText(field.getFieldType());
		this.fieldName.setText(field.getFieldName());
		this.typeRefLabel.setText("@" + field.getJVMSignatureReference());
		this.nameRefLabel.setText("@" + field.getFieldNameReference());
	}
	
	private synchronized void acceptChange() {
		String oldField = field.toString();
		
		int access = 0;
		if (this.publicAccessCheckbox.getState()) {
			access ^= Modifier.PUBLIC;
		} else if (this.protectedAccessCheckbox.getState()) {
			access ^= Modifier.PROTECTED;
		} else if (this.privateAccessCheckbox.getState()) {
			access ^= Modifier.PRIVATE;
		}
		
		if (this.staticAccessCheckbox.getState()) {
			access ^= Modifier.STATIC;
		}
		if (this.finalAccessCheckbox.getState()) {
			access ^= Modifier.FINAL;
		}
		if (this.transientAccessCheckbox.getState()) {
			access ^= Modifier.TRANSIENT;
		}
		if (this.volatileAccessCheckbox.getState()) {
			access ^= Modifier.VOLATILE;
		}
		
		field.setAccessModifiers(access);
		field.setFieldType(fieldType.getText());
		field.setFieldName(fieldName.getText());
		
		String newField = field.toString();
		
		propertyChangeSupport.firePropertyChange("JavaField", oldField, newField);
	}
	
}
