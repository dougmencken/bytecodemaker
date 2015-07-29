/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;

/**
 *	<code>LWButton</code>
 *
 *	@version 1.2
 */

public class LWButton extends Component implements MouseListener {
	
	private String label;
	private transient ActionListener actionListener;
	private String actionCommand;
	
	private boolean pressed = false;
	private boolean dragExit = false;
	private int labelPosX, labelPosY;
	
	public LWButton() {
		this("", Color.white);
	}
	
	public LWButton(String label) {
		this(label, Color.white);
	}
	
	public LWButton(String label, Color background) {
		this.label = label;
		setBackground(background);
		setForeground(Color.black);
		setFont(new Font("Geneva", Font.BOLD, 9));
		
		this.setActionCommand(label);
		super.addMouseListener(this);
		
		super.setSize(this.getMinimumSize());
		this.repaint();
	}
	
	public String getLabel() { return label; }
	
	public void setLabel(String newLabel) {
		if (!newLabel.equals(this.label)) {
			this.label = newLabel;
			repaint();
		}
	}
	
	public String getActionCommand() { return actionCommand; }
	
	public void setActionCommand(String command) {
		this.actionCommand = command;
	}
	
	public Dimension getMinimumSize() {
		FontMetrics fm = getFontMetrics(getFont());
		int width = fm.stringWidth(label) + 8;
		int height = fm.getAscent() + 8;
		
		return new Dimension(width, height);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(getSize());
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		
		FontMetrics fm = getFontMetrics(getFont());
		if ((width >= getMinimumSize().width) &&
				(height >= getMinimumSize().height)) {
			int label_width = fm.stringWidth(label);
			int label_height = fm.getAscent();
			this.labelPosX = width/2 - label_width/2;
			this.labelPosY = height/2 + label_height/2 - 1;
		}
	}
	
	public void addActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.add(actionListener, listener);
		super.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
	public void removeActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void mousePressed(MouseEvent e) {
		if (!super.isEnabled()) return;
		
		pressed = true;
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		if (!super.isEnabled()) return;
		
		dragExit = false;
		
		if (pressed) {
			pressed = false;
			repaint(); 
			
			if (actionListener != null) {
				actionListener.actionPerformed(
					new ActionEvent(
						this,
						ActionEvent.ACTION_PERFORMED,
						actionCommand
					)
				);
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) {
		if (!super.isEnabled()) return;
		
		if (dragExit) {
			pressed = true;
			repaint();
		}
	}
	
	public void mouseExited(MouseEvent e) {
		if (!super.isEnabled()) return;
		
		if (pressed == true) {
			pressed = false;
			repaint();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		Color foreground = super.getForeground();
		if (!super.isEnabled()) {
			foreground = super.getBackground().darker().darker();
		}
		
		Dimension r = getSize();
		
		/* "outside" */
		g.setColor(getBackground());
		
		// top
		g.drawLine(0, 0, 2, 0);
		g.drawLine(r.width-3, 0, r.width-1, 0);
		g.drawLine(0, 1, 0, 2);
		g.drawLine(r.width-1, 1, r.width-1, 2);
		
		// bottom
		g.drawLine(0, r.height-1, 2, r.height-1);
		g.drawLine(r.width-3, r.height-1, r.width-1, r.height-1);
		g.drawLine(0, r.height-2, 0, r.height-3);
		g.drawLine(r.width-1, r.height-2, r.width-1, r.height-3);
		
		/* button's borders */
		g.setColor(foreground);
		// top
		g.drawLine(3, 0, r.width-4, 0);
		g.drawLine(1, 1, 2, 1);
		g.drawLine(r.width-3, 1, r.width-2, 1);
		g.drawLine(1, 2, 1, 2);
		g.drawLine(r.width-2, 2, r.width-2, 2);
		
		// left & right
		g.drawLine(0, 3, 0, r.height-4);
		g.drawLine(r.width-1, 3, r.width-1, r.height-4);
		
		// bottom
		g.drawLine(1, r.height-3, 1, r.height-3);
		g.drawLine(r.width-2, r.height-3, r.width-2, r.height-3);
		g.drawLine(1, r.height-2, 2, r.height-2);
		g.drawLine(r.width-3, r.height-2, r.width-2, r.height-2);
		g.drawLine(3, r.height-1, r.width-4, r.height-1);
		
		/* background */
		if (pressed) {
			g.setColor(foreground);
		} else {
			g.setColor(getBackground());
		}
		g.drawLine(3, 1, r.width-4, 1);
		g.drawLine(2, 2, r.width-3, 2);
		g.drawLine(2, r.height-3, r.width-3, r.height-3);
		g.drawLine(3, r.height-2, r.width-4, r.height-2);
		g.fillRect(1, 3, r.width-2, r.height-6);
		
		/* label */
		g.setFont(getFont());
		if (pressed) {
			dragExit = true;
			g.setColor(getBackground());
		} else {
			g.setColor(foreground);
		}
		g.drawString(label, labelPosX, labelPosY);
	}
	
}
