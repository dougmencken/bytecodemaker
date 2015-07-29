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
 *	<code>RoundButton</code>
 *
 *	@version 1.1
 */

public class RoundButton extends Component implements MouseListener {
	
	private String label;
	private transient ActionListener actionListener;
	private String actionCommand;
	
	private boolean pressed = false;
	private boolean dragExit = false;
	
	public RoundButton() { 
		this("", Color.black, Color.white);
	}
	
	public RoundButton(Color foreground, Color background) { 
		this("", foreground, background);
	}
	
	public RoundButton(String label) {
		this(label, Color.black, Color.white);
	}
	
	public RoundButton(String label, Color foreground, Color background) {
		this.label = label;
		this.actionCommand = label;
		
		///*** TODO)))) check if font(s) is(are) available ***///
		super.setFont(new Font("Charcoal", Font.PLAIN, 12));
		super.setFont(new Font("Chicago", Font.PLAIN, 12));
		super.setForeground(foreground);
		super.setBackground(background);
		
		addMouseListener(this);
		setSize(getPreferredSize());
		repaint();
	}
	
	public String getLabel() { return label; }
	
	public void setLabel(String label) {
		this.label = label;
		invalidate();
		repaint();
	}
	
	public String getActionCommand() { return actionCommand; }
	
	public void setActionCommand(String command) {
		this.actionCommand = command;
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	// Note that to avoid deadlocks, paint is *not* synchronized
	public void paint(Graphics g) {
		Color foreground = super.getForeground();
		if (!super.isEnabled()) {
			foreground = super.getBackground().darker().darker();
		}
		
		int min_wh = Math.min(getSize().width - 1, getSize().height - 1);
		
		if (pressed) {
			dragExit = true;
			g.setColor(getBackground().darker().darker());
		} else {
			g.setColor(getBackground());
		}
		g.fillArc(0, 0, min_wh, min_wh, 0, 360);
		
		g.setColor(foreground);
		g.drawArc(0, 0, min_wh, min_wh, 0, 360);
		
		Font thisFont = super.getFont();
		if (thisFont != null) {
			FontMetrics fm = super.getFontMetrics(thisFont);
			int labelX = (min_wh - fm.stringWidth(label) + 2)/2;
			int labelY = (min_wh + fm.getMaxAscent() - 2)/2;
			
			g.setFont(thisFont);
			g.drawString(label, labelX, labelY);
		}
	}
	
	public Dimension getPreferredSize() {
		Font thisFont = super.getFont();
		
		if (thisFont != null) {
			FontMetrics fm = getFontMetrics(thisFont);
			int max = Math.max(
				fm.stringWidth(label),
				fm.getMaxAscent() + fm.getMaxDescent()
			);
			return new Dimension(max + 40, max + 40);
		} else {
			return getMinimumSize();
		}
	}
	
	public Dimension getMinimumSize() { return new Dimension(50, 50); }
	
	public void addActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.add(actionListener, listener);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
	public void removeActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public boolean isInside(int x, int y) {
		int mx = getSize().width/2;
		int my = getSize().height/2;
		return (((mx-x)*(mx-x) + (my-y)*(my-y)) <= mx*mx);
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
	
}
