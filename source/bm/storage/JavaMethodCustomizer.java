// ===========================================================================
//	JavaMethodCustomizer.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.lang.reflect.Modifier;

import douglas.mencken.util.FontUtilities;
import douglas.mencken.beans.*;
import douglas.mencken.icons.NoteIcon;
import douglas.mencken.tools.DialogFactory;

/**
 *	@version	1.03f1
 *	@since		Bytecode Maker 0.5.8
 */

public class JavaMethodCustomizer extends Panel
implements Customizer, TextListener, ItemListener {
	
	private PropertyChangeSupport propertyChangeSupport;
	private JavaMethod method;
	
	private LWCheckbox publicAccessCheckbox;
	private LWCheckbox protectedAccessCheckbox;
	private LWCheckbox privateAccessCheckbox;
	private LWCheckbox defaultAccessCheckbox;
	
	private LWCheckbox abstractAccessCheckbox;
	private LWCheckbox staticAccessCheckbox;
	private LWCheckbox finalAccessCheckbox;
	private LWCheckbox nativeAccessCheckbox;
	private LWCheckbox synchronizedAccessCheckbox;
	
	private TextField returnType;
	private TextField methodName;
	private LWLabel returnTypeRefLabel;
	private LWLabel nameRefLabel;
	
	public JavaMethodCustomizer() {
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
		
		abstractAccessCheckbox = new LWCheckbox("abstract", false);
		abstractAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		abstractAccessCheckbox.addItemListener(this);
		
		staticAccessCheckbox = new LWCheckbox("static", false);
		staticAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		staticAccessCheckbox.addItemListener(this);
		
		finalAccessCheckbox = new LWCheckbox("final", false);
		finalAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		finalAccessCheckbox.addItemListener(this);
		
		nativeAccessCheckbox = new LWCheckbox("native", true);
		nativeAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		nativeAccessCheckbox.addItemListener(this);
		
		synchronizedAccessCheckbox = new LWCheckbox("synchronized");
		synchronizedAccessCheckbox.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		synchronizedAccessCheckbox.addItemListener(this);
		
		this.returnType = new TextField();
		this.returnType.setFont(new Font("Geneva", Font.PLAIN, 10));
		this.returnType.addTextListener(this);
		
		this.methodName = new TextField();
		this.methodName.setFont(new Font("Geneva", Font.PLAIN, 10));
		this.methodName.addTextListener(this);
		
		this.publicAccessCheckbox.setBounds(70, 20, 100, 20);
		this.protectedAccessCheckbox.setBounds(70, 40, 100, 20);
		this.privateAccessCheckbox.setBounds(70, 60, 100, 20);
		this.defaultAccessCheckbox.setBounds(70, 80, 100, 20);
		
		this.abstractAccessCheckbox.setBounds(200, 20, 100, 20);
		this.staticAccessCheckbox.setBounds(200, 40, 100, 20);
		this.finalAccessCheckbox.setBounds(200, 60, 100, 20);
		this.nativeAccessCheckbox.setBounds(200, 80, 100, 20);
		this.synchronizedAccessCheckbox.setBounds(200, 100, 100, 20);
		
		returnType.setBounds(95, 125, 250, 20);
		methodName.setBounds(95, 150, 250, 20);
		
		LWLabel accessLabel = new LWLabel("Access: ", LWLabel.LEFT);
		LWLabel typeLabel = new LWLabel("Return type: ", LWLabel.LEFT);
		LWLabel nameLabel = new LWLabel("Name: ", LWLabel.LEFT);
		accessLabel.setLocation(5, 20);
		typeLabel.setLocation(5, 125);
		nameLabel.setLocation(5, 150);
		
		this.returnTypeRefLabel = new LWLabel("@-1", LWLabel.LEFT);
		this.nameRefLabel = new LWLabel("@-1", LWLabel.LEFT);
		this.returnTypeRefLabel.setForeground(Color.gray);
		this.nameRefLabel.setForeground(Color.gray);
		this.returnTypeRefLabel.setLocation(355, 127);
		this.nameRefLabel.setLocation(355, 152);
		
		super.add(accessLabel);
		super.add(typeLabel);
		super.add(nameLabel);
		
		super.add(this.publicAccessCheckbox);
		super.add(this.protectedAccessCheckbox);
		super.add(this.privateAccessCheckbox);
		super.add(this.defaultAccessCheckbox);
		
		super.add(this.abstractAccessCheckbox);
		super.add(this.staticAccessCheckbox);
		super.add(this.finalAccessCheckbox);
		super.add(this.nativeAccessCheckbox);
		super.add(this.synchronizedAccessCheckbox);
		
		super.add(this.returnType);
		super.add(this.methodName);
		super.add(this.returnTypeRefLabel);
		super.add(this.nameRefLabel);
		
		super.setSize(this.getPreferredSize());
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(/* width */ 395, /* height */ 200);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.addPropertyChangeListener(pcl);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.removePropertyChangeListener(pcl);
	}
	
	public void setObject(Object bean) {
		this.method = (JavaMethod)bean;
		this.updateCheckboxes();
		
		this.returnType.setText(method.getReturnType());
		this.methodName.setText(method.getMethodName());
		this.returnTypeRefLabel.setText("@" + method.getJVMSignatureReference());
		this.nameRefLabel.setText("@" + method.getMethodNameReference());
	}
	
	private void updateCheckboxes() {
		int access = this.method.getAccessModifiers();
		if (Modifier.isPublic(access)) {
			this.publicAccessCheckbox.setState(true);
		} else if (Modifier.isProtected(access)) {
			this.protectedAccessCheckbox.setState(true);
		} else if (Modifier.isPrivate(access)) {
			this.privateAccessCheckbox.setState(true);
		} else {
			this.defaultAccessCheckbox.setState(true);
		}
		
		this.abstractAccessCheckbox.setState(Modifier.isAbstract(access));
		this.staticAccessCheckbox.setState(Modifier.isStatic(access));
		this.finalAccessCheckbox.setState(Modifier.isFinal(access));
		this.nativeAccessCheckbox.setState(Modifier.isNative(access));
		this.synchronizedAccessCheckbox.setState(Modifier.isSynchronized(access));
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.acceptChange();
	}
	
	public void textValueChanged(TextEvent e) {
		this.acceptChange();
	}
	
	private synchronized void acceptChange() {
		String oldMethod = this.method.toString();
		
		int access = 0;
		if (this.publicAccessCheckbox.getState()) {
			access ^= Modifier.PUBLIC;
		} else if (this.protectedAccessCheckbox.getState()) {
			access ^= Modifier.PROTECTED;
		} else if (this.privateAccessCheckbox.getState()) {
			access ^= Modifier.PRIVATE;
		}
		
		if (this.abstractAccessCheckbox.getState()) {
			access ^= Modifier.ABSTRACT;
		}
		if (this.staticAccessCheckbox.getState()) {
			access ^= Modifier.STATIC;
		}
		if (this.finalAccessCheckbox.getState()) {
			access ^= Modifier.FINAL;
		}
		if (this.nativeAccessCheckbox.getState()) {
			access ^= Modifier.NATIVE;
		}
		if (this.synchronizedAccessCheckbox.getState()) {
			access ^= Modifier.SYNCHRONIZED;
		}
		
		this.method.setAccessModifiers(access, /* show dialogs */ true);
		this.updateCheckboxes();
		
		this.method.setReturnType(returnType.getText());
		this.method.setMethodName(methodName.getText());
		
		String newMethod = this.method.toString();
		
		propertyChangeSupport.firePropertyChange("JavaMethod", oldMethod, newMethod);
	}
	
}
