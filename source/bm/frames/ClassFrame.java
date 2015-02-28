// ===========================================================================
//	ClassFrame.java (part of douglas.mencken.bm.frames package)
// ===========================================================================

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import douglas.mencken.util.FontUtilities;
import douglas.mencken.beans.LWLabel;
import douglas.mencken.beans.support.CustomizerFrame;
import douglas.mencken.tools.UsefulModalDialogs;

import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.storage.JavaConstantPool;
import douglas.mencken.bm.storage.JavaField;
import douglas.mencken.bm.storage.JavaMethod;
import douglas.mencken.bm.menu.FileMenu;

/**
 *	<code>ClassFrame</code>
 *
 *	@version	1.41f3
 */

public class ClassFrame extends Frame /*TitledRectWindow*/
implements WindowListener, PropertyChangeListener {
	
	private static ClassFrame currentFrame = null;
	private JavaClass theClass = null;
	
	LWLabel className;
	LWLabel class_interface;
	LWLabel constantCount;
	LWLabel fieldCount;
	LWLabel methodCount;
	
	public ClassFrame() {
		super("");
		//super.setPopupMenu(douglas.mencken.bm.menus.BMMenuBar.makePopupMenu());
		
		currentFrame = this;
		super.addWindowListener(this);
		
		this.className = new LWLabel("");
		this.className.setFont(new Font("Geneva", Font.BOLD, 12));
		
		this.class_interface = new LWLabel("");
		this.class_interface.setFont(FontUtilities.createFont(FontUtilities.GENEVA_9));
		this.class_interface.setForeground(Color.darkGray);
		
		this.constantCount = new LWLabel("");
		this.constantCount.setFont(FontUtilities.createFont(FontUtilities.GENEVA_9));
		
		this.fieldCount = new LWLabel("");
		this.fieldCount.setFont(FontUtilities.createFont(FontUtilities.GENEVA_9));
		
		this.methodCount = new LWLabel("");
		this.methodCount.setFont(FontUtilities.createFont(FontUtilities.GENEVA_9));
		
		super.setLayout(new GridBagLayout());
		this.updateContents();
		
		super.setLocation(5, 5);
		super.setResizable(false);
	}
	
	private void addComponents() {
		GridBagLayout gridbag = (GridBagLayout)super.getLayout();
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.insets = new Insets(10, 10, 3, 5);
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(class_interface, c);
			super.add(class_interface);
		}
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 2;
			c.insets = new Insets(0, 10, 10, 10);
			c.anchor = GridBagConstraints.NORTH;
			gridbag.setConstraints(className, c);
			super.add(className);
		}
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 3;
			c.insets = new Insets(0, 5, 3, 5);
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(constantCount, c);
			super.add(constantCount);
		}
		
		{
			GridBagConstraints  c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 4;
			c.insets = new Insets(0, 5, 3, 5);
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(fieldCount, c);
			super.add(fieldCount);
		}
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 5;
			c.insets = new Insets(0, 5, 10, 5);
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(methodCount, c);
			super.add(methodCount);
		}
	}
	
	private void clearComponents() {
		super.removeAll();
		
		this.constantCount.setText("");
		this.fieldCount.setText("");
		this.methodCount.setText("");
		this.class_interface.setText("");
		this.className.setText("");
	}
	
	public void setClass(JavaClass newClass) {
		if (this.theClass == newClass) return;
		this.theClass = newClass;
		
		this.updateContents();
	}
	
	public void editClass() {
		if (this.theClass == null) {
			super.getToolkit().beep();
			return;
		}
		
		try {
			String title = (theClass.isInterface()) ? "interface " : "class ";
			CustomizerFrame frame = CustomizerFrame.customizeBean(
				theClass, this, title + theClass.getClassName(), this
			);
			
			frame.setResizable(false);
			frame.setVisible(true);
		} catch (Exception exc) {
			UsefulModalDialogs.tellAboutInternalError(
				exc.getClass().getName() + ": " + exc.getMessage()
			);
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (theClass.isInterface()) {
			this.class_interface.setText("(interface)");
		} else {
			this.class_interface.setText("(class)");
		}
		this.class_interface.setSize(class_interface.getPreferredSize());
		
		className.setText(theClass.getClassName());
		className.setSize(className.getPreferredSize());
		
		super.setSize(super.getLayout().preferredLayoutSize(this));
	}
	
	/**
	 *	Ensures that all labels are updated with correct information.
	 */
	public void updateContents() {
		if (theClass != null) {
			// check if constant pool is not 'null'
			JavaConstantPool pool = theClass.getConstantPool();
			if (pool == null) {
				this.windowClosing(null);
				throw new InternalError(
					"No constant pool for '" + theClass.toString() + "'"
				);
			}
			
			// remove all and add updated components
			super.removeAll();
			this.addComponents();
			
			this.className.setText(theClass.getClassName());
			if (theClass.isInterface()) {
				this.class_interface.setText("(interface)");
			} else {
				this.class_interface.setText("(class)");
			}
			
			this.constantCount.setText("Constants: " + pool.getConstantCount());
			this.fieldCount.setText("Fields: " + theClass.getFieldCount());
			this.methodCount.setText("Methods: " + theClass.getMethodCount());
		} else if (theClass == null) {
			this.clearComponents();
			
			Label label = new Label("No class");
			label.setForeground(Color.darkGray);
			label.setFont(new Font("Geneva", Font.BOLD, 12));
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(/* top */ 10, /* left */ 20, /* bottom */ 10, /* right */ 20);
			((GridBagLayout)super.getLayout()).setConstraints(label, c);
			super.add(label);
		}
		
		super.pack();
	}
	
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	public void windowClosing(WindowEvent evt) {
		((FileMenu)(super.getMenuBar().getMenu(0))).close();
	}
	
	public static ClassFrame getCurrentFrame() {
		if (currentFrame == null) {
			currentFrame = new ClassFrame();
		}
		
		return currentFrame;
	}
	
}
