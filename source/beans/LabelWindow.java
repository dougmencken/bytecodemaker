/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.*;
import douglas.mencken.util.InvisibleFrame;
import douglas.mencken.util.WindowUtilities;

/**
 *	<code>LabelWindow</code>
 *	Window used to show, display some information, and then hide.
 *
 *	@version 1.01f
 */

public class LabelWindow extends Window {
	
	private Label label;
	private InvisibleFrame theOwner;
	
	public LabelWindow() {
		this(new InvisibleFrame(), "", new Font("Geneva", Font.BOLD, 10));
	}
	
	public LabelWindow(String label) {
		this(new InvisibleFrame(), label, new Font("Geneva", Font.BOLD, 10));
	}
	
	/**
	 *	Constructs a new <code>LabelWindow</code> object with
	 *	a specified label and font.
	 */
	public LabelWindow(String label, Font font) {
		this(new InvisibleFrame(), label, font);
	}
	
	protected LabelWindow(InvisibleFrame owner, String label, Font font) {
		super(owner);
		this.theOwner = owner;
		super.setVisible(false);
		
		super.setBackground(Color.white);
		super.setForeground(Color.black);
		super.setLayout(new BorderLayout());
		
		this.label = new Label(label);
		this.label.setFont(font);
		BorderBox bbox = new BorderBox();
		bbox.add(this.label);
		
		super.add(bbox);
		super.pack();
		
		Point center = WindowUtilities.getCenterLocation(this);
		super.setLocation(center.x + 5, center.y - 55);
	}
	
	public void setVisible(boolean newVisible) {
		if (newVisible) {
			// otherwise the window may be partially (or fully) hidden
			// under the other windows
			this.theOwner.toFront();
			
			// otherwise you can see the empty window with no contents
			super.paintAll(super.getGraphics());
		}
		
		super.setVisible(newVisible);
	}
	
	public void showThenDispose(int ms) {
		this.setVisible(true);
		this.dispose(ms);
	}
	
	public void dispose(int ms) {
		if (ms > 0) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException ignored) {}
		}
		
		this.dispose();
	}
	
	public void dispose() {
		super.dispose();
		theOwner.dispose();
	}
	
}