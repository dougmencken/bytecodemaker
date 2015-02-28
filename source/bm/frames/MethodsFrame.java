// ===========================================================================
//	MethodsFrame.java (part of douglas.mencken.bm.frames package)
//		public class MethodsFrame
//		class MethodsFrameMenuBar
//	
// ===========================================================================

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Modifier;
import java.beans.*;

import douglas.mencken.tools.UsefulModalDialogs;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.beans.ButtonedLabel;
import douglas.mencken.beans.support.*;

import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.storage.JavaMethod;

/**
 *	<code>MethodsFrame</code>
 *
 *	@version	1.40f1
 */

public class MethodsFrame extends Frame
implements ActionListener, AdjustmentListener, PropertyChangeListener {
	
	protected static final int MAXIMUM_HEIGHT = 520;
	
	JavaClass clazz = null;
	String[] methodDeclarations;
	private ButtonedLabel[] methodLabels;
	private transient int methodIndex = -1;
	
	/**
	 *	Used in manipulations with scrollbar.
	 */
	private Panel listPanel;
	
	public MethodsFrame(WindowListener listener) {
		super("Methods");
		new MethodsFrameMenuBar(this, listener);
		
		super.setLayout(new BorderLayout());
		super.setSize(40, 20);
		super.setResizable(false);
	}
	
	public void setClass(JavaClass clazz) {
		if (this.clazz != clazz) {
			this.clazz = clazz;
			this.methodDeclarations = clazz.getMethodDeclarations();
			
			StringBuffer titleBuf = new StringBuffer();
			if (clazz != null) {
				titleBuf.append(clazz.getClassName()).append(": ");
			}
			titleBuf.append("Methods");
			super.setTitle(titleBuf.toString());
			
			this.updateContents();
			((MethodsFrameMenuBar)super.getMenuBar()).updateMenu();
		}
	}
	
	public void updateContents() {
		super.removeAll();
		
		int count = this.methodDeclarations.length;
		
		this.methodLabels = new ButtonedLabel[count];
		this.listPanel = new Panel(new GridLayout(count, 1));
		
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				String[] declarationParts = ClassUtilities.splitDeclaration(
					this.methodDeclarations[i]
				);
				String throwsString = declarationParts[4].trim();
				
				String[] labels = new String[] {
					(declarationParts[0] + ' ' + declarationParts[1]).trim(),
					declarationParts[2] + declarationParts[3],
					throwsString,
				};
				String[] buttons = new String[] { "Edit", "Code" };
				
				ButtonedLabel bLabel = new ButtonedLabel(
					ButtonedLabel.BUTTONS_LEFT_RIGHT,
					labels, buttons
				);
				bLabel.addBottomLine();
				bLabel.setActionCommand("METHOD" + i);
				bLabel.addActionListener(this);
				
				this.listPanel.add(bLabel);
				this.methodLabels[i] = bLabel;
			}
		} else {
			Label noMethods = new Label("No methods", Label.CENTER);
			noMethods.setFont(new Font("Geneva", Font.BOLD, 10));
			noMethods.setForeground(Color.darkGray);
			this.listPanel.add(noMethods);
		}
		
		Dimension size = this.getPreferredSize();
		if (size.height > MAXIMUM_HEIGHT) {
			size.height = MAXIMUM_HEIGHT;
		}
		super.setSize(size);
		this.addComponents();
		
		super.invalidate();
		super.validate();
	}
	
	protected void addComponents() {
		int height = this.getPreferredSize().height;
		Panel coverPanel = this.listPanel;
		
		if (height > MAXIMUM_HEIGHT) {
			Scrollbar scroller = new Scrollbar(
				Scrollbar.VERTICAL,
				0, 480, 0, height - 40
			);
			scroller.setUnitIncrement(25);
			scroller.setBlockIncrement(480);
			scroller.addAdjustmentListener(this);
			super.add("East", scroller);
			
			coverPanel = new Panel(new FlowLayout());
			coverPanel.add(this.listPanel);
		}
		
		super.add("Center", coverPanel);
	}
	
	/** 
	 *	Returns the preferred size of this window.
	 */
	public Dimension getPreferredSize() {
		int width = 40;
		int height = 0;
		
		Component[] allComponents = this.listPanel.getComponents();
		int count = allComponents.length;
		
		for (int i = 0; i < count; i++) {
			if (allComponents[i] instanceof ButtonedLabel) {
				Dimension bLabelSize = ((ButtonedLabel)allComponents[i]).getPreferredSize();
				if (bLabelSize.width > width) {
					width = bLabelSize.width;
				}
				height += bLabelSize.height;
			} else if (allComponents[i] instanceof Label) {
				if (((Label)allComponents[i]).getText().equals("No methods")) {
					return new Dimension(240, 100);
				}
			}
		}
		
		return new Dimension(width + 20, height);
	}
	
	/** 
	 *	Returns the minimum size of this window.
	 */
	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}
	
	void addMethod(JavaMethod method) {
		if (this.clazz != null) {
			this.clazz.addMethod(method);
		}
	}
	
	void removeMethod(int index) {
		if (this.clazz != null) {
			this.clazz.removeMethod(index);
		}
	}
	
	public void adjustmentValueChanged(AdjustmentEvent evt) {
		Adjustable adj = evt.getAdjustable();
		if (!(adj instanceof Scrollbar)) return;
		
		this.listPanel.invalidate();
		this.listPanel.setLocation(0, -evt.getValue());
		this.listPanel.validate();
	}
	
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		this.methodIndex = Integer.parseInt(StringUtilities.getBefore(cmd.substring(6), '$'));
		
		if (cmd.endsWith("$Code")) {
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			JVMInstructionSetEditor.showEditor(this.clazz.getMethod(this.methodIndex), this.methodIndex);
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else if (cmd.endsWith("$Edit")) {
			try {
				JavaMethod method = this.clazz.getMethod(this.methodIndex);
				CustomizerFrame frame = CustomizerFrame.customizeBean(
					method, this, method.getNameWithParameters(), this
				);
				
				frame.setResizable(false);
				frame.setVisible(true);
			} catch (Exception exc) {
				UsefulModalDialogs.tellAboutInternalError(
					exc.getClass().getName() + ": " + exc.getMessage()
				);
			}
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String declaration = (String)evt.getNewValue();
		String[] declarationParts = ClassUtilities.splitDeclaration(declaration);
		String throwsString = declarationParts[4].trim();
		
		String[] labels = new String[] {
			declarationParts[0] + ' ' + declarationParts[1],
			declarationParts[2] + declarationParts[3],
			throwsString,
		};
		this.methodLabels[this.methodIndex].setLabels(labels);
		
		((MethodsFrameMenuBar)super.getMenuBar()).updateMenu();
		
		Dimension size = this.getPreferredSize();
		if (size.height > MAXIMUM_HEIGHT) {
			size.height = MAXIMUM_HEIGHT;
		}
		
		super.setSize(size);
		super.validateTree();
	}
	
}


class MethodsFrameMenuBar extends MenuBar implements ActionListener {
	
	private MethodsFrame owner;
	private WindowListener listener;
	private Menu methodsMenu;
	private MenuItem removeMethod;
	
	MethodsFrameMenuBar(MethodsFrame frame, WindowListener listener) {
		MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		
		Menu file = new Menu("File");
		file.add(close);
		super.add(file);
		
		MenuItem addMethod = new MenuItem("Add Method...");
		addMethod.setActionCommand("ADD");
		addMethod.addActionListener(this);
		this.removeMethod = new MenuItem("Remove Method...");
		this.removeMethod.setActionCommand("REMOVE");
		this.removeMethod.addActionListener(this);
		
		this.methodsMenu = new Menu("Methods");
		this.methodsMenu.add(addMethod);
		this.methodsMenu.add(removeMethod);
		super.add(methodsMenu);
		
		frame.setMenuBar(this);
		this.owner = frame;
		this.listener = listener;
		
		this.updateMenu();
	}
	
	void updateMenu() {
		synchronized (super.getTreeLock()) {
			this.removeMethod.setEnabled(false);
			
			int itemCount = methodsMenu.getItemCount() - 2;
			for (int i = 0; i < itemCount; i++) {
				methodsMenu.remove(2);
			}
			
			if (this.owner.clazz != null) {
				methodsMenu.addSeparator();
				int count = this.owner.methodDeclarations.length;
				
				if (count > 0) {
					Menu currentMethod;
					
					for (int i = 0; i < count; i++) {
						MenuItem edit = new MenuItem("Edit");
						MenuItem code = new MenuItem("Code");
						edit.setActionCommand("METHOD" + i + "$Edit");
						code.setActionCommand("METHOD" + i + "$Code");
						edit.addActionListener(this.owner);
						code.addActionListener(this.owner);
						
						String[] declarationParts = ClassUtilities.splitDeclaration(
							this.owner.methodDeclarations[i]
						);
						currentMethod = new Menu(
							/* name + parameters */ declarationParts[2] + declarationParts[3]
						);
						currentMethod.add(edit);
						currentMethod.add(code);
						
						methodsMenu.add(currentMethod);
					}
					
					this.removeMethod.setEnabled(true);
				} else {
					MenuItem noMethods = new MenuItem("(no methods)");
					methodsMenu.add(noMethods);
					noMethods.setEnabled(false);
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		if (c.equals("CLOSE")) {
			listener.windowClosing(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
		} else if (c.equals("ADD")) {
			NewBeanCustomizerDialog newDialog = new NewBeanCustomizerDialog(
				"Method", JavaMethod.class
			);
			newDialog.setVisible(true);
			
			JavaMethod newJavaMethod = (JavaMethod)newDialog.getBean();
			if (newJavaMethod != null) {
				owner.addMethod(newJavaMethod);
				
				this.updateMenu();
				owner.updateContents();
				
				try {
					ClassFrame.getCurrentFrame().updateContents();
				} catch (IncompleteException exc) {}
			}
		} else if (c.equals("REMOVE")) {
			// ... show list of methods ... //
			owner.removeMethod(0);
			this.updateMenu();
			owner.updateContents();
			
			try {
				ClassFrame.getCurrentFrame().updateContents();
			} catch (IncompleteException exc) {}
		}
	}
	
}