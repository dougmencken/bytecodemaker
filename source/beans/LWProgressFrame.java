/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.beans.LWLabel;
import douglas.mencken.util.InvisibleFrame;
import douglas.mencken.util.WindowUtilities;
import douglas.mencken.util.event.*;

/**
 *	<code>LWProgressFrame</code>
 *
 *	@version 1.2
 */

public class LWProgressFrame extends Frame implements ActionListener {
	
	protected LWLabel label;
	protected LWProgressBar pbar;
	protected LWButton stopButton;
	
	private transient CancelListener listener = null;
	private transient CancelKeyListener keyListener = null;
	
	private static LWProgressFrame currentFrame =
				new LWProgressFrame("", new Font("Monaco", Font.PLAIN, 9));
	
	/**
	 *	The main constructor.
	 */
	public LWProgressFrame(String labelText, Font labelFont) {
		super("Progress");
		currentFrame = this;
		
		this.label = new LWLabel(labelText, labelFont);
		this.pbar = new LWProgressBar();
		this.stopButton = new LWButton("  Stop  ");
		stopButton.setEnabled(false);
		stopButton.addActionListener(this);
		
		GridBagLayout gridbag = new GridBagLayout();
		super.setLayout(gridbag);
		this.addComponents();
		
		super.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.updateBounds();
		
		super.setResizable(false);
	}
	
	private void addComponents() {
		GridBagLayout gridbag = (GridBagLayout)super.getLayout();
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.gridwidth = 2;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(5, 5, 3, 5);
			gridbag.setConstraints(label, c);
		}
		super.add(label);
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridy = 1;
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(2, 5, 5, 5);
			gridbag.setConstraints(pbar, c);
		}
		super.add(pbar);
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridy = 1;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(2, 5, 5, 5);
			gridbag.setConstraints(stopButton, c);
		}
		super.add(stopButton);
	}
	
	public LWProgressFrame(Font labelFont) {
		this("", labelFont);
	}
	
	public LWProgressFrame(String labelText) {
		this(labelText, new Font("Monaco", Font.PLAIN, 9));
	}
	
	public LWProgressFrame() {
		this("", new Font("Monaco", Font.PLAIN, 9));
	}
	
	public void setLayout(LayoutManager layout) {
		/* do nothing */
	}
	
	public void addCancelListener(CancelListener listener) {
		if (listener != null) {
			this.listener = listener;
			this.keyListener = new CancelKeyListener();
			keyListener.addCancelListener(listener);
			super.addKeyListener(keyListener);
			
			stopButton.setEnabled(true);
		}
	}
	
	public void removeCancelListener(CancelListener listener) {
		if (this.listener == listener) {
			this.listener = null;
		}
		super.removeKeyListener(keyListener);
		stopButton.setEnabled(false);
	}
	
	public void setVisible(boolean visibility) {
		pbar.setValue((visibility) ? pbar.getMinimum() : pbar.getMaximum());
		super.setVisible(visibility);
		
		if (visibility) {
			super.paintAll(super.getGraphics());
		} else {
			// reset progress bar colors to default when hiding
			pbar.useDefaultColors();
		}
	}
	
	public void setProgressBarForeground(Color fore) {
		pbar.setForeground(fore);
	}
	
	public void setProgressBarBackground(Color back) {
		pbar.setBackground(back);
	}
	
	public void setValue(int newValue) {
		pbar.setValue(newValue);
		super.toFront();
		super.paintAll(super.getGraphics());
	}
	
	public void changeValue(int valueCount, int value) {
		pbar.changeValue(valueCount, value);
		super.toFront();
		super.paintAll(super.getGraphics());
	}
	
	public void changeValue(int barStart, int barEnd, int valueCount, int value) {
		pbar.changeValue(barStart, barEnd, valueCount, value);
		super.toFront();
		super.paintAll(super.getGraphics());
	}
	
	public void setMinimum(int newMin) {
		pbar.setMinimum(newMin);
		super.paintAll(super.getGraphics());
	}
	
	public void setMaximum(int newMax) {
		pbar.setMaximum(newMax);
		super.paintAll(super.getGraphics());
	}
	
	public double getPercentComplete() {
		return pbar.getPercentComplete();
	}
	
	public int getValue() { return pbar.getValue(); }
	public int getMinimum() { return pbar.getMinimum(); }
	public int getMaximum() { return pbar.getMaximum(); }
	
	public void setLabelText(String labelText) {
		label.setText(labelText);
		label.setSize(label.getPreferredSize());
		
		this.updateBounds();
	}
	
	public String getLabelText() {
		return this.label.getText();
	}
	
	public void setLabelFont(Font f) {
		label.setFont(f);
		label.setSize(label.getPreferredSize());
		
		this.updateBounds();
	}
	
	public Font getLabelFont() {
		return this.label.getFont();
	}
	
	public void updateBounds() {
		//super.removeAll();
		//super.paintAll(super.getGraphics());
		//this.addComponents();
		
		super.pack();
		Point pos = WindowUtilities.getCenterLocation(this);
		super.setLocation(pos.x, pos.y - 120);
		
		super.invalidate();
		super.doLayout();
		super.validate();
	}
	
	public void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.doCancel();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		this.doCancel();
	}
	
	protected void doCancel() {
		if (listener != null) {
			listener.operationCanceled(new CancelEvent(this));
			super.setVisible(false);
		}
	}
	
	/**
	 *	(static method)
	 */
	public static LWProgressFrame getCurrentFrame() {
		return currentFrame;
	}
	
}
