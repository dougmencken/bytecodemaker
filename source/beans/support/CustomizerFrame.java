// ===========================================================================
//	CustomizerFrame.java (part of douglas.mencken.beans.support package)
// ===========================================================================

package douglas.mencken.beans.support;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

/**
 *	<code>CustomizerFrame</code>
 *
 *	@version	1.02f
 */

public class CustomizerFrame extends Frame
implements ActionListener, WindowListener {
	
	private Component customizerComponent;
	private Button doneButton;
	protected static final int vpad = 25;
	
	public CustomizerFrame(Window parent, Customizer customizer, Object target) {
		this(parent, customizer, target, customizer.getClass().getName());
	}
	
	public CustomizerFrame(Window parent, Customizer customizer, Object target, String title) {
		super(title);
		
		super.setBackground(Color.white);
		super.setLayout(null);
		super.addWindowListener(this);
		
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("File");
		{
			MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
			close.setActionCommand("CLOSE");
			close.addActionListener(this);
			file.add(close);
		}
		menuBar.add(file);
		this.setMenuBar(menuBar);
		
		if (!(customizer instanceof Component)) {
			throw new IllegalArgumentException("customizer isn't a component");
		}
		
		this.customizerComponent = (Component)customizer;
		super.add(customizerComponent);
		
		this.doneButton = new Button("Done");
		this.doneButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.doneButton.setActionCommand("CLOSE");
		this.doneButton.addActionListener(this);
		super.add(doneButton);
		
		Dimension d = customizerComponent.getPreferredSize();
		int width = d.width;
		int height = vpad + d.height + 35;
		if (width < 200) {
			width = 200;
		}
		
		Point location = new Point(100, 100);
		if (parent != null) {
			location.x = parent.getLocation().x + 30;
			location.y = parent.getLocation().y + 100;
		}
		super.setBounds(location.x, location.y, width, height);
	}
	
	public void doLayout() {
		int width = super.getSize().width;
		int height = super.getSize().height;
		customizerComponent.setBounds(0, vpad, width, height - (vpad+35));
		doneButton.setBounds((width - 80)/2, height - 25, 80, 20);
	}
	
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		super.dispose();
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("CLOSE")) {
			this.windowClosing(null);
		}
	}
	
	
	/**
	 *	An easiest way to get the CustomizerFrame is to use
	 *	this static method.
	 *
	 *	@param	parent		(may be null)
	 *	@param	listener	(may be null; if null, the 'Window parent' is
	 *						 checked to be an instance of PropertyChangeListener)
	 */
	public static CustomizerFrame customizeBean(Object bean,
												Window parent, String title,
												PropertyChangeListener listener)
	throws Exception {
		BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass());
		Class customizerClass = null;
		
		try {
			BeanDescriptor bd = beaninfo.getBeanDescriptor();
			customizerClass = bd.getCustomizerClass();
		} catch (Exception exc) {
			throw new Exception("couldn't get customizer class: " + exc.getMessage());
		}
		
		if (customizerClass != null) {
			Customizer customizer = null;
			try {
				customizer = (Customizer)customizerClass.newInstance();
				customizer.setObject(bean);
			} catch (Exception exc) {
				throw new Exception("couldn't instantiate customizer: " + exc.getMessage());
			}
			
			if (listener != null) {
				customizer.addPropertyChangeListener(listener);
			} else if ((parent != null) && (parent instanceof PropertyChangeListener)) {
				customizer.addPropertyChangeListener((PropertyChangeListener)parent);
			}
			
			CustomizerFrame frame = new CustomizerFrame(parent, customizer, bean, title);
			return frame;
		}
		
		throw new Exception(
			"Bean " + bean.getClass().getName() + " { " + bean + " } has no customizer"
		);
	}
	
}