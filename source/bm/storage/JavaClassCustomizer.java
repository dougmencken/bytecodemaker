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

import douglas.mencken.beans.*;
import douglas.mencken.icons.QuestionIcon;
import douglas.mencken.util.*;
import douglas.mencken.tools.DialogFactory;
import douglas.mencken.tools.UsefulMessageDialogs;

import douglas.mencken.bm.storage.constants.BMErrorStrings;

/**
 *	<code>JavaClassCustomizer</code>
 *
 *	@version	1.1
 *	@since		Bytecode Maker A.6
 */

public class JavaClassCustomizer extends LWTabbedPane
implements Customizer, ItemListener {
	
	private PropertyChangeSupport propertyChangeSupport;
	private JavaClass clazz;
	private boolean classIsNew = false;
	private String oldDeclaration;
	
	// declaration labels
	private LWLabel declarationLabel1;
	private LWLabel declarationLabel2;
	
	/**
	 *	Initializes a new <code>JavaClassCustomizer</code>.
	 */
	public JavaClassCustomizer() {
		super(null);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		super.setCommonComponent(this.createCommonComponent());
		
		ClassCustomizerPanel classPanel = new ClassCustomizerPanel(this);
		InterfaceCustomizerPanel interfacePanel = new InterfaceCustomizerPanel(this);
		
		super.addTab("Class", classPanel);
		super.addTab("Interface", interfacePanel);
		super.setSelectedIndex(0);
		super.addItemListener(this);
		
		this.updateDeclarationLabel();
		super.setSize(this.getPreferredSize());
	}
	
	private Component createCommonComponent() {
		Panel common = new Panel(null);
		
		this.declarationLabel1 = new LWLabel(LWLabel.LEFT);
		this.declarationLabel2 = new LWLabel(LWLabel.LEFT);
		
		// Bounds: x, y, width, height (location, size)
		int width = JavaClassCustomizerPart.JAVA_CLASS_CUSTOMIZER_PART_SIZE.width;
		declarationLabel1.setBounds(5, 0, width - 10, 12);
		declarationLabel2.setBounds(5, 14, width - 10, 12);
		
		common.add(declarationLabel1);
		common.add(declarationLabel2);
		common.setSize(width, 30);
		
		return common;
	}
	
	public Dimension getPreferredSize() {
		//	width:	+ 4 for border;
		//	height:	+ commonComponent.height for common component,
		//		+ 18 for tabs and other,
		//		+ 2 for border, +10 for some space
		int width = JavaClassCustomizerPart.JAVA_CLASS_CUSTOMIZER_PART_SIZE.width;
		int height = JavaClassCustomizerPart.JAVA_CLASS_CUSTOMIZER_PART_SIZE.height;
		
		return new Dimension(
			width + 4,
			height + 30 + super.getCommonComponent().getSize().height
		);
	}
	
	public void updateDeclarationLabel() {
		JavaClassCustomizerPart part =
				(JavaClassCustomizerPart)super.getSelectedComponent();
		String[] declarationLabels = part.getDeclarationLabels();
		declarationLabel1.setText(declarationLabels[0]);
		declarationLabel2.setText(declarationLabels[1]);
	}
	
	public void fireChange() {
		JavaClassCustomizerPart part =
				(JavaClassCustomizerPart)super.getSelectedComponent();
		
		// fire property change
		this.clazz = part.getJavaClass();
		String newValue = clazz.toString();
		
		propertyChangeSupport.firePropertyChange("JavaClass", this.oldDeclaration, newValue);
		this.oldDeclaration = newValue;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.addPropertyChangeListener(pcl);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.removePropertyChangeListener(pcl);
	}
	
	public void setObject(Object bean) {
		this.clazz = (JavaClass)bean;
		this.oldDeclaration = this.clazz.toString();
		this.classIsNew = (this.clazz.getConstantPool() == null);
		
		if (clazz.isInterface()) {
			super.setSelectedIndex(1);
		} else {
			super.setSelectedIndex(0);
		}
		
		JavaClassCustomizerPart part =
				(JavaClassCustomizerPart)super.getSelectedComponent();
		part.setJavaClass(this.clazz);
		
		this.updateDeclarationLabel();
	}
	
	public void itemStateChanged(ItemEvent evt) {
		String className = this.clazz.getClassName();
		if (className.equals("java.lang.Object")) {
			// java.lang.Object cannot be 'interface'!
			super.setSelectedIndex(0);
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		
		int selectedIndex = super.getSelectedIndex();
		if (selectedIndex == -1) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		
		boolean _interface = (selectedIndex == 1);
		
		if (!this.classIsNew) {
			StringBuffer message = new StringBuffer("Convert from ");
			if (_interface) {
				message.append("'class' to 'interface'?");
			} else {
				message.append("'interface' to 'class'?");
			}
			
			int yes_no = DialogFactory.doTwoButtonsDialog(
				new QuestionIcon(), message.toString(), "  Yes  ", "  No  "
			);
			if (yes_no == 1) {
				// don't convert
				super.setSelectedIndex((selectedIndex == 1) ? 0 : 1);
				return;
			}
		}
		
		// convert
		this.clazz.setInterface(_interface);
		
		// update frames
		// ! Warning: in Java Bean Editors, it may be null or not present
		// try {
		//	douglas.mencken.bm.cover.BMEnvironment.updateFrames();
		// } catch (Throwable ignored) {}
		
		if (className.equals("UntitledClass") && _interface) {
			this.clazz.setClassName("UntitledInterface");
		} else if (className.equals("UntitledInterface") && !_interface) {
			this.clazz.setClassName("UntitledClass");
		}
		
		JavaClassCustomizerPart dst =
				(JavaClassCustomizerPart)super.getComponentAt(selectedIndex);
		dst.setJavaClass(this.clazz);
		
		this.updateDeclarationLabel();
	}
	
}


// ------------------------------------------------------------------------------------------

interface JavaClassCustomizerPart extends ActionListener, TextListener, ItemListener {
	
	public static final Dimension JAVA_CLASS_CUSTOMIZER_PART_SIZE = new Dimension(620, 180);
	
	//public boolean doCheck();
	public String[] getDeclarationLabels();
	public void setJavaClass(JavaClass bean);
	public JavaClass getJavaClass();
		
}


// ------------------------------------------------------------------------------------------

class ClassCustomizerPanel extends Panel implements JavaClassCustomizerPart {
	
	private JavaClass clazz;
	private JavaClassCustomizer parent;
	
	private TextField classNameField;
	private TextField superclassNameField;
	
	private List interfacesList;
	private LWCheckbox serializableCheckbox;
	private LWCheckbox runnableCheckbox;
	private LWButton addInterfaceButton;
	private LWButton removeInterfaceButton;
	
	private LWCheckbox publicAccessCheckbox;
	private LWCheckbox defaultAccessCheckbox;
	private LWCheckbox finalAccessCheckbox;
	private LWCheckbox synchronizedAccessCheckbox;
	private LWCheckbox abstractAccessCheckbox;
	
	ClassCustomizerPanel(JavaClassCustomizer parent) {
		super();
		this.parent = parent;
		super.setLayout(null);
		
		initComponents();
		addComponents();
		
		super.setSize(JAVA_CLASS_CUSTOMIZER_PART_SIZE);
	}
	
	private void initComponents() {
		classNameField = new TextField("UntitledClass");
		classNameField.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		classNameField.addTextListener(this);
		superclassNameField = new TextField("java.lang.Object");
		superclassNameField.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		superclassNameField.addTextListener(this);
		
		interfacesList = new List();
		interfacesList.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		
		serializableCheckbox = new LWCheckbox("java.io.Serializable");
		serializableCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		serializableCheckbox.addItemListener(this);
		
		runnableCheckbox = new LWCheckbox("java.lang.Runnable");
		runnableCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		runnableCheckbox.addItemListener(this);
		
		addInterfaceButton = new LWButton("Add...");
		addInterfaceButton.setActionCommand("ADD_INTERFACE");
		addInterfaceButton.addActionListener(this);
		
		removeInterfaceButton = new LWButton("Remove");
		removeInterfaceButton.setEnabled(false);
		removeInterfaceButton.setActionCommand("REMOVE_INTERFACE");
		removeInterfaceButton.addActionListener(this);
		
		// access checkboxes
		LWCheckboxGroup group = new LWCheckboxGroup();
		publicAccessCheckbox = new LWCheckbox("public", group, true);
		publicAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		publicAccessCheckbox.addItemListener(this);
		
		defaultAccessCheckbox = new LWCheckbox("default", group, false);
		defaultAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		defaultAccessCheckbox.addItemListener(this);
		
		finalAccessCheckbox = new LWCheckbox("final", false);
		finalAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		finalAccessCheckbox.addItemListener(this);
		
		synchronizedAccessCheckbox = new LWCheckbox("synchronized", true);
		synchronizedAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		synchronizedAccessCheckbox.addItemListener(this);
		
		abstractAccessCheckbox = new LWCheckbox("abstract");
		abstractAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		abstractAccessCheckbox.addItemListener(this);
	}
	
	private void addComponents() {
		classNameField.setSize(200, 20);
		superclassNameField.setSize(200, 20);
		interfacesList.setSize(165, 100);
		
		Dimension prefSize1 = removeInterfaceButton.getPreferredSize();
		addInterfaceButton.setSize(prefSize1.width + 20, prefSize1.height);
		removeInterfaceButton.setSize(prefSize1.width + 20, prefSize1.height);
		
		// labels & locations
		
		LWLabel nameLabel = new LWLabel("Name:", LWLabel.LEFT);
		nameLabel.setLocation(5, 12);
		
		LWLabel superclassLabel = new LWLabel("Superclass:", LWLabel.LEFT);
		superclassLabel.setLocation(5, 42);
		
		LWLabel interfacesLabel = new LWLabel("Interfaces:", LWLabel.LEFT);
		interfacesLabel.setLocation(295, 12);
		
		classNameField.setLocation(85, 10);
		superclassNameField.setLocation(85, 40);
		
		interfacesList.setLocation(450, 10);
		serializableCheckbox.setLocation(305, 35);
		runnableCheckbox.setLocation(305, 55);
		
		addInterfaceButton.setLocation(455, 115);
		removeInterfaceButton.setLocation(610 - removeInterfaceButton.getSize().width, 115);
		
		LWLabel accessLabel = new LWLabel("Access:", LWLabel.LEFT);
		accessLabel.setLocation(5, 80);
		publicAccessCheckbox.setLocation(80, 80);
		defaultAccessCheckbox.setLocation(80, 100);
		finalAccessCheckbox.setLocation(80, 120);
		synchronizedAccessCheckbox.setLocation(80, 140);
		abstractAccessCheckbox.setLocation(80, 160);
		
		super.add(nameLabel);
		super.add(classNameField);
		super.add(superclassLabel);
		super.add(superclassNameField);
		super.add(interfacesLabel);
		super.add(interfacesList);
		super.add(serializableCheckbox);
		super.add(runnableCheckbox);
		super.add(addInterfaceButton);
		super.add(removeInterfaceButton);
		super.add(accessLabel);
		super.add(publicAccessCheckbox);
		super.add(defaultAccessCheckbox);
		super.add(finalAccessCheckbox);
		super.add(synchronizedAccessCheckbox);
		super.add(abstractAccessCheckbox);
	}
	
	public void setJavaClass(JavaClass bean) {
		if (bean.isInterface()) {
			throw new IllegalArgumentException(
				"ClassCustomizerPanel can't customize 'interface'"
			);
		}
		
		// class and superclass name
		classNameField.setText(bean.getClassName());
		superclassNameField.setText(bean.getSuperclassName());
		
		// interfaces list
		String[] interfaces = bean.getInterfaces();
		int count = interfaces.length;
		
		interfacesList.removeAll();
		for (int i = 0; i < count; i++) {
			String _interface = interfaces[i];
			if (_interface.equals("java.lang.Runnable")) {
				runnableCheckbox.setState(true);
			} else if (_interface.equals("java.io.Serializable")) {
				serializableCheckbox.setState(true);
			} else {
				interfacesList.add(_interface);
				removeInterfaceButton.setEnabled(true);
			}
		}
		
		// access modifiers
		int classAccess = bean.getAccessModifiers();
		finalAccessCheckbox.setState(Modifier.isFinal(classAccess));
		synchronizedAccessCheckbox.setState(Modifier.isSynchronized(classAccess));
		abstractAccessCheckbox.setState(Modifier.isAbstract(classAccess));
		
		if (Modifier.isPublic(classAccess)) {
			publicAccessCheckbox.setState(true);
		} else {
			defaultAccessCheckbox.setState(true);
		}
		
		// store the class
		this.clazz = bean;
	}
	
	public JavaClass getJavaClass() {
		return this.clazz;
	}
	
	private synchronized void acceptChange() {
		// change class
//		clazz.setClassName(classNameField.getText());
//		clazz.setSuperclassName(superclassNameField.getText());
		
		StringBuffer buf = new StringBuffer();
		String[] interfaces = interfacesList.getItems();
		int count = interfaces.length;
		
		for (int i = 0; i < count; i++) {
			buf.append(interfaces[i]);
			buf.append('\n');
		}
		if (runnableCheckbox.getState()) {
			buf.append("java.lang.Runnable\n");
		}
		if (serializableCheckbox.getState()) {
			buf.append("java.io.Serializable\n");
		}
		
		clazz.setInterfaces(StringUtilities.extractStrings(buf));
		
		int classAccess = 0;
		if (finalAccessCheckbox.getState()) {
			classAccess |= Modifier.FINAL;
		}
		if (synchronizedAccessCheckbox.getState()) {
			classAccess |= Modifier.SYNCHRONIZED;
		}
		if (abstractAccessCheckbox.getState()) {
			classAccess |= Modifier.ABSTRACT;
		}
		if (publicAccessCheckbox.getState()) {
			classAccess |= Modifier.PUBLIC;
		}
		
		clazz.setAccessModifiers(classAccess);
		
		// fire change and update
		parent.fireChange();
		parent.updateDeclarationLabel();
	}
	
	public void textValueChanged(TextEvent e) {
		this.acceptChange();
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.acceptChange();
	}
	
	public String[] getDeclarationLabels() {
		StringBuffer declarationBuffer1 = new StringBuffer();
		StringBuffer declarationBuffer2 = new StringBuffer();
		
		if (publicAccessCheckbox.getState()) {
			declarationBuffer1.append("public ");
		}
		if (finalAccessCheckbox.getState()) {
			declarationBuffer1.append("final ");
		}
		if (synchronizedAccessCheckbox.getState()) {
			declarationBuffer1.append("synchronized ");
		}
		if (abstractAccessCheckbox.getState()) {
			declarationBuffer1.append("abstract ");
		}
		
		declarationBuffer1.append("class ");
		declarationBuffer1.append(classNameField.getText());
		
		String superclassName = superclassNameField.getText();
		if (superclassName.length() != 0) {
			declarationBuffer1.append(" extends ");
			declarationBuffer1.append(superclassName);
		}
		
		int interfaceCount = 0;
		if (serializableCheckbox.getState()) {
			if (interfaceCount == 0) {
				declarationBuffer2.append("implements ");
			} else {
				declarationBuffer2.append(", ");
			}
			declarationBuffer2.append("java.io.Serializable");
			interfaceCount++;
		}
		if (runnableCheckbox.getState()) {
			if (interfaceCount == 0) {
				declarationBuffer2.append("implements ");
			} else {
				declarationBuffer2.append(", ");
			}
			declarationBuffer2.append("java.lang.Runnable");
			interfaceCount++;
		}
		
		String[] list = interfacesList.getItems();
		if ((list != null) && (list.length != 0)) {
			if (interfaceCount == 0) {
				declarationBuffer2.append("implements ");
			} else {
				declarationBuffer2.append(", ");
			}
			
			int count = list.length;
			for (int i = 0; i < count; i++) {
				declarationBuffer2.append(list[i]);
				if ((i+1) != count) {
					declarationBuffer2.append(", ");
				}
			}
		}
		
		String[] ret = new String[2];
		ret[0] = declarationBuffer1.toString();
		ret[1] = declarationBuffer2.toString();
		return ret;
	}
	
	/*public boolean doCheck() {
		String className = classNameField.getText();
		String superclassName = superclassNameField.getText();
		
		if (className.length() == 0) {
			UsefulMessageDialogs.doWarningDialog("Please specify the name of new class");
			return false;
		}
		
		if ((className.indexOf(' ') != -1) || (superclassName.indexOf(' ') != -1)) {
			UsefulMessageDialogs.sayAboutError(8, BMErrorStrings.getErrorString(8));
			return false;
		}
		
		if (!ClassUtilities.isJavaIdentifier(className) ||
			 !ClassUtilities.isJavaIdentifier(superclassName)) {
			UsefulMessageDialogs.sayAboutError(9, BMErrorStrings.getErrorString(9));
			return false;
		}
		
		return true;
	}*/
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals("ADD_INTERFACE")) {
			String result = DialogFactory.askForOneString("Add interface:");
			if ((result != null) && (result != "")) {
				if (result.equals("java.lang.Runnable")) {
					runnableCheckbox.setState(true);
				} else if (result.equals("java.io.Serializable")) {
					serializableCheckbox.setState(true);
				} else {
					boolean isValid = true;
					
					if (!ClassUtilities.isJavaIdentifier(result)) {
						UsefulMessageDialogs.sayAboutError(9, BMErrorStrings.getErrorString(9));
						isValid = false;
					}
					
					int itemCount = interfacesList.getItemCount();
					for (int i = 0; i < itemCount; i++) {
						if (result.equals(interfacesList.getItem(i))) {
							isValid = false;
							break;
						}
					}
					
					if (isValid) {
						interfacesList.add(result);
						interfacesList.select(itemCount);
						removeInterfaceButton.setEnabled(true);
					}
				}
				
				this.acceptChange();
			}
		} else if (command.equals("REMOVE_INTERFACE")) {
			try {
				interfacesList.remove(interfacesList.getSelectedIndex());
				interfacesList.select(interfacesList.getItemCount() - 1);
			} catch (Exception ignored) {}
			
			if (interfacesList.getItemCount() < 1) {
				removeInterfaceButton.setEnabled(false);
			}
			
			this.acceptChange();
		}
	}
	
}


// ------------------------------------------------------------------------------------------

class InterfaceCustomizerPanel extends Panel implements JavaClassCustomizerPart {
	
	private JavaClass clazz;
	private JavaClassCustomizer parent;
	
	private TextField interfaceNameField;
	private List superinterfacesList;
	private LWButton addButton;
	private LWButton removeButton;
	private LWCheckbox defaultAccessCheckbox;
	private LWCheckbox finalAccessCheckbox;
	
	InterfaceCustomizerPanel(JavaClassCustomizer parent) {
		super();
		this.parent = parent;
		super.setLayout(null);
		
		initComponents();
		addComponents();
		
		super.setSize(JAVA_CLASS_CUSTOMIZER_PART_SIZE);
	}
	
	private void initComponents() {
		interfaceNameField = new TextField("UntitledInterface");
		interfaceNameField.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		interfaceNameField.addTextListener(this);
		
		superinterfacesList = new List();
		superinterfacesList.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		
		addButton = new LWButton("Add...");
		addButton.setActionCommand("ADD_INTERFACE");
		addButton.addActionListener(this);
		
		removeButton = new LWButton("Remove");
		removeButton.setEnabled(false);
		removeButton.setActionCommand("REMOVE_INTERFACE");
		removeButton.addActionListener(this);
		
		// access checkboxes
		defaultAccessCheckbox = new LWCheckbox("default", false);
		defaultAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		defaultAccessCheckbox.addItemListener(this);
		
		finalAccessCheckbox = new LWCheckbox("final", false);
		finalAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		finalAccessCheckbox.addItemListener(this);
	}
	
	private void addComponents() {
		interfaceNameField.setSize(200, 20);
		superinterfacesList.setSize(165, 100);
		
		Dimension prefSize1 = removeButton.getPreferredSize();
		addButton.setSize(prefSize1.width + 20, prefSize1.height);
		removeButton.setSize(prefSize1.width + 20, prefSize1.height);
		
		// labels & locations
		
		LWLabel nameLabel = new LWLabel("Name:", LWLabel.LEFT);
		nameLabel.setLocation(5, 12);
		interfaceNameField.setLocation(85, 10);
		
		LWLabel superinterfacesLabel = new LWLabel("Superinterfaces:", LWLabel.LEFT);
		superinterfacesLabel.setLocation(325, 12);
		superinterfacesList.setLocation(450, 10);
		
		addButton.setLocation(455, 115);
		removeButton.setLocation(610 - removeButton.getSize().width, 115);
		
		LWLabel accessLabel = new LWLabel("Access:", LWLabel.LEFT);
		accessLabel.setLocation(5, 80);
		defaultAccessCheckbox.setLocation(80, 80);
		finalAccessCheckbox.setLocation(80, 100);
		
		super.add(nameLabel);
		super.add(interfaceNameField);
		super.add(superinterfacesLabel);
		super.add(superinterfacesList);
		super.add(addButton);
		super.add(removeButton);
		super.add(accessLabel);
		super.add(defaultAccessCheckbox);
		super.add(finalAccessCheckbox);
	}
	
	public void setJavaClass(JavaClass bean) {
		if (!bean.isInterface()) {
			throw new IllegalArgumentException(
				"InterfaceCustomizerPanel can't customize 'class'"
			);
		}
		
		// class and superclass name
		interfaceNameField.setText(bean.getClassName());
		
		// interfaces list
		String[] superinterfaces = bean.getInterfaces();
		int count = superinterfaces.length;
		
		superinterfacesList.removeAll();
		for (int i = 0; i < count; i++) {
			superinterfacesList.add(superinterfaces[i]);
			removeButton.setEnabled(true);
		}
		
		// access modifiers
		int classAccess = bean.getAccessModifiers();
		defaultAccessCheckbox.setState(!Modifier.isPublic(classAccess));
		finalAccessCheckbox.setState(Modifier.isFinal(classAccess));
		
		// store the class
		this.clazz = bean;
	}
	
	public JavaClass getJavaClass() {
		return this.clazz;
	}
	
	private synchronized void acceptChange() {
		// change class
		clazz.setClassName(interfaceNameField.getText());
		clazz.setInterfaces(superinterfacesList.getItems());
		
		int classAccess = Modifier.INTERFACE;
		if (!defaultAccessCheckbox.getState()) {
			classAccess |= Modifier.PUBLIC;
		}
		if (finalAccessCheckbox.getState()) {
			classAccess |= Modifier.FINAL;
		}
		clazz.setAccessModifiers(classAccess);
		
		// fire change and update
		parent.fireChange();
		parent.updateDeclarationLabel();
	}
	
	public void textValueChanged(TextEvent e) {
		this.acceptChange();
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.acceptChange();
	}
	
	public String[] getDeclarationLabels() {
		StringBuffer declarationBuffer1 = new StringBuffer();
		StringBuffer declarationBuffer2 = new StringBuffer();
		
		if (!defaultAccessCheckbox.getState()) {
			declarationBuffer1.append("public ");
		}
		if (finalAccessCheckbox.getState()) {
			declarationBuffer1.append("final ");
		}
		
		declarationBuffer1.append("interface ");
		declarationBuffer1.append(interfaceNameField.getText());
		
		String[] list = superinterfacesList.getItems();
		if ((list != null) && (list.length != 0)) {
			int count = list.length;
			if (count == 1) {
				declarationBuffer1.append(" extends ");
				declarationBuffer1.append(list[0]);
			} else {
				declarationBuffer2.append("extends ");
				
				for (int i = 0; i < count; i++) {
					declarationBuffer2.append(list[i]);
					if ((i+1) != count) {
						declarationBuffer2.append(", ");
					}
				}
			}
		}
		
		String[] ret = new String[2];
		ret[0] = declarationBuffer1.toString();
		ret[1] = declarationBuffer2.toString();
		return ret;
	}
	
	/*public boolean doCheck() {
		String interfaceName = interfaceNameField.getText();
		
		if (interfaceName.length() == 0) {
			UsefulMessageDialogs.doWarningDialog("Please specify the name of new interface");
			return false;
		}
		
		if (interfaceName.indexOf(' ') != -1) {
			UsefulMessageDialogs.sayAboutError(8, BMErrorStrings.getErrorString(8));
			return false;
		}
		
		if (!ClassUtilities.isJavaIdentifier(interfaceName)) {
			UsefulMessageDialogs.sayAboutError(9, BMErrorStrings.getErrorString(9));
			return false;
		}
		
		return true;
	}*/
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals("ADD_INTERFACE")) {
			String result = DialogFactory.askForOneString("Add superinterface:");
			if ((result != null) && (result != "")) {
				boolean isValid = true;
				
				if (!ClassUtilities.isJavaIdentifier(result)) {
					UsefulMessageDialogs.sayAboutError(9, BMErrorStrings.getErrorString(9));
					isValid = false;
				}
				
				int count = superinterfacesList.getItemCount();
				for (int i = 0; i < count; i++) {
					if (result.equals(superinterfacesList.getItem(i))) {
						isValid = false;
						break;
					}
				}
				
				if (isValid) {
					superinterfacesList.add(result);
					superinterfacesList.select(count);
					removeButton.setEnabled(true);
				}
				
				this.acceptChange();
			}
		} else if (command.equals("REMOVE_INTERFACE")) {
			try {
				superinterfacesList.remove(superinterfacesList.getSelectedIndex());
				superinterfacesList.select(superinterfacesList.getItemCount() - 1);
			} catch (Exception ignored) {}
			
			if (superinterfacesList.getItemCount() < 1) {
				removeButton.setEnabled(false);
			}
			
			this.acceptChange();
		}
	}
	
}