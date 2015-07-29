/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

import java.awt.*;
import java.awt.event.*;

/**
 *	<code>WindowUtilities</code>
 *
 *	@version 1.08
 */

public final class WindowUtilities extends Object {
	
	public static final Dimension BUTTON_DIMENSION = new Dimension(75, 20);
	public static final Dimension WIDE_BUTTON_DIMENSION = new Dimension(100, 20);
	
	
	/**
	 *	Don't let anyone instantiate this class.
	 */
	private WindowUtilities() { super(); }
	
	
	/**
	 *	Note:	1) any parameter here may be 'null':
	 *			   <i>listener = null</i> if it is not required,
	 *			   <i>panelSize = null</i> to adjust size later,
	 *			   <i>additionalComponent = null</i> if you don't need it.
	 *			2) action commands set by this method:
	 *			   <i>"OK"<i> for OK button, <i>"CANCEL"<i> for Cancel button.
	 */
	public static Panel createOKCancelButtonPanel(	ActionListener listener,
							Dimension panelSize,
							Component additionalComponent) {
		Panel buttonPanel = new Panel(null);
		if (panelSize != null) {
			buttonPanel.setSize(panelSize);
		}
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setSize(BUTTON_DIMENSION);
		Button okButton = new Button("OK");
		okButton.setSize(BUTTON_DIMENSION);
		
		if (listener != null) {
			okButton.setActionCommand("OK");
			okButton.addActionListener(listener);
			cancelButton.setActionCommand("CANCEL");
			cancelButton.addActionListener(listener);
		}
		
		Point[] locations_OK_Cancel = getPreferredOKCancelButtonLocations(
			buttonPanel, okButton, cancelButton
		);
		okButton.setLocation(locations_OK_Cancel[0]);
		cancelButton.setLocation(locations_OK_Cancel[1]);
		
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);
		
		if (additionalComponent != null) {
			additionalComponent.setLocation(10, locations_OK_Cancel[0].y);
			buttonPanel.add(additionalComponent);
		}
		
		return buttonPanel;
	}
	
	public static Point[] getPreferredOKCancelButtonLocations(Container container, Button ok, Button cancel) {
		Dimension containerSize = container.getSize();
		Dimension okButtonSize = ok.getSize();
		Dimension cancelButtonSize = cancel.getSize();
		
		Point[] locations = new Point[2];
		locations[0] = new Point(
			containerSize.width - okButtonSize.width - 10,
			containerSize.height - okButtonSize.height - 10
		);
		
		locations[1] = new Point(
			containerSize.width - okButtonSize.width - cancelButtonSize.width - 20,
			containerSize.height - cancelButtonSize.height - 10
		);
		
		return locations;
	}
	
	public static Point getCenterLocation(Window window) {
		Dimension componentSize = window.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point pos = new Point();
		
		pos.x = ((screenSize.width / 2) - (componentSize.width / 2));
		pos.y = ((screenSize.height / 2) - (componentSize.height / 2)) - 30;
		return pos;
	}
	
	public static void addToWidth(Window window, int aw) {
		Dimension d = window.getSize();
		window.setSize((d.width + aw), d.height);
	}
	
	public static void addToHeight(Window window, int ah) {
		Dimension d = window.getSize();
		window.setSize(d.width, (d.height + ah));
	}
	
	public static void addToSize(Window window, int aw, int ah) {
		Dimension d = window.getSize();
		window.setSize((d.width + aw), (d.height + ah));
	}
	
	public static void addToSize(Window window, Dimension ad) {
		addToSize(window, ad.width, ad.height);
	}
	
	public static Insets makeBoxInsets(int value) {
		return new Insets(value, value, value, value);
	}
	
	public static Point getScreenCenter() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point((int)(screenSize.width / 2), (int)(screenSize.height / 2));
	}
	
	public static void addLabels(Window window, String[] ss, Font f) {
		int len = ss.length;
		window.setLayout(new GridLayout(len, 1));
		for (int i = 0; i < len; i++) {
			Label label = new Label(ss[i]);
			if (f != null) {
				label.setFont(f);
			}
			window.add(label);
		}
		
		window.pack();
	}
	
	public static Rectangle getPreferredDialogBounds(Panel panel, boolean withImage, Component component) {
		// width
		int mw = (withImage) ? 32 : 0;
		int width = panel.getSize().width + mw;
		
		if (component != null) {
			int cWidth = component.getSize().width;
			if (cWidth > width) {
				width = cWidth + mw;
			}
		}
		
		if (width < 60) width = 60;
		width += 20;
		
		// height
		int height = panel.getSize().height + 10;
		if (component != null) {
			height += component.getPreferredSize().height;
		}
		
		// location
		Point center = WindowUtilities.getScreenCenter();
		int mx = (withImage) ? 30 : 10;
		
		int x = Math.round(center.x - (width/2) - mx);
		int y = center.y - 125;
		
		return new Rectangle(x, y, width + 32, height + 32 + 32);
	}
	
}
