/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans.support;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import douglas.mencken.util.WindowUtilities;
import douglas.mencken.util.InvisibleFrame;

/**
 *	<code>NewBeanCustomizerDialog</code>
 *
 *	@version	1.1
 */

public class NewBeanCustomizerDialog extends Dialog
implements ActionListener, PropertyChangeListener {
	
	private boolean cancelFlag = false;
	private Object theBean = null;
	private Class beanClass;
	
	/**
	 *	The default title of dialog is "New " + 'class name'.
	 */
	public NewBeanCustomizerDialog(Class beanClass) {
		this(beanClass.getName(), beanClass);
	}
	
	/**
	 *	@param	whatNew		the title of dialog will be "New " + 'whatNew'.
	 *	@param	beanClass	the bean to be instantiated and customized.
	 */
	public NewBeanCustomizerDialog(String whatNew, Class beanClass) {
		super(new InvisibleFrame(), "New " + whatNew, true);
		super.setBackground(Color.white);
		
		Component customizer = null;
		try {
			// bean, bean info, bean customizer
			this.beanClass = beanClass;
			this.theBean = beanClass.newInstance();
			
			BeanDescriptor bd = Introspector.getBeanInfo(beanClass).getBeanDescriptor();
			Customizer c = (Customizer)(bd.getCustomizerClass().newInstance());
			c.setObject(this.theBean);
			c.addPropertyChangeListener(this);
			
			customizer = (Component)c;
		} catch (IntrospectionException introExc) {
			throw new InternalError("IntrospectionException: " + introExc.getMessage());
		} catch (InstantiationException instaExc) {
			throw new InternalError("InstantiationException" + instaExc.getMessage());
		} catch (IllegalAccessException accessEx) {
			throw new InternalError("IllegalAccessException" + accessEx.getMessage());
		}
		if (customizer == null) {
			/* Toolkit.getDefaultToolkit().beep(); */
			throw new IllegalArgumentException("Can't get customizer for " + beanClass);
		}
		
		int buttonPanelHeight = WindowUtilities.BUTTON_DIMENSION.height*2 + 5;
		
		// adjust size and location
		Dimension preferredCustomizerSize = customizer.getPreferredSize();
		super.setSize(
			preferredCustomizerSize.width + 10,
			preferredCustomizerSize.height + buttonPanelHeight + 10
		);
		super.setResizable(false);
		super.setLocation(WindowUtilities.getCenterLocation(this));
		
		// the button panel
		Panel buttonPanel = WindowUtilities.createOKCancelButtonPanel(
			this,
			new Dimension(super.getSize().width, buttonPanelHeight),
			null
		);
		
		super.add("South", buttonPanel);
		if (customizer != null) {
			super.add("Center", customizer);
		}
		super.doLayout();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {}
	
	/**
	 *	@return		the newly created and customized bean.
	 *
	 */
	public Object getBean() {
		if (cancelFlag) {
			return null;
		} else {
			return this.theBean;
		}
	}
	
	protected void doCancel() {
		this.cancelFlag = true;
		super.dispose();
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals("OK")) {
			super.dispose();
		} else if (command.equals("CANCEL")) {
			this.doCancel();
		}
	}
	
}
