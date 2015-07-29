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
 *	<code>LWCheckbox</code>
 *
 *	@version 1.1
 */

public class LWCheckbox extends Component implements MouseListener, ItemSelectable {
	
	String label;
	boolean state;
	LWCheckboxGroup group;
	private transient ItemListener itemListener;
	
	private int boxSize;
	
	private int mover;
	private boolean pressed;
	private boolean dragExit;
	
	public LWCheckbox(String label, boolean state, LWCheckboxGroup group) {
		this.label = label;
		this.state = state;
		this.group = group;
		if (state && (group != null)) {
			group.setSelectedCheckbox(this);
		}
		
		super.setFont(new Font("Geneva", Font.PLAIN, 10));
		addMouseListener(this);
		
		this.boxSize = getFont().getSize() + 2;
		super.setSize(getPreferredSize());
		repaint();
	}
	
	public LWCheckbox(String label, LWCheckboxGroup group, boolean state) {
		this(label, state, group);
	}
	
	public LWCheckbox(String label, boolean state) {
		this(label, state, null);
	}
	
	public LWCheckbox(String label) {
		this(label, false, null);
	}
	
	public LWCheckbox() {
		this("", false, null);
	}
	
	public Dimension getMinimumSize() {
		int addToWidth = getFontMetrics(getFont()).stringWidth(label) + 4;
		return new Dimension(boxSize + addToWidth, boxSize);
	}
	
	public Dimension getPreferredSize() {
		int addToWidth = getFontMetrics(getFont()).stringWidth(label) + 12;
		return new Dimension(boxSize + addToWidth, boxSize + 8);
	}
	
	public String getLabel() { return label; }
	
	public void setLabel(String newLabel) {
		this.label = newLabel;
		repaint();
	}
	
	public void setEnabled(boolean enabled) {
		boolean oldEnabled = super.isEnabled();
		super.setEnabled(enabled);
		
		// firePropertyChange("enabled", oldEnabled, enabled);
		if (enabled != oldEnabled) {
			super.repaint();
		}
    }
    
	public boolean getState() { return state; }
	
	public void setState(boolean newState) {
		if (newState != state) {
			this.state = newState;
			repaint();
		}
		
		if (group != null) {
			if (state) {
				group.setSelectedCheckbox(this);
			} else if (group.getSelectedCheckbox() == this) {
				state = true;
			}
		}
	}
	
	public LWCheckboxGroup getCheckboxGroup() { return group; }
	
	public void setCheckboxGroup(LWCheckboxGroup g) {
		LWCheckboxGroup group = this.group;
		if (group != null) {
			group.setSelectedCheckbox(null);
		}
		
		this.group = g;
	}
	
	public boolean isGrouped() {
		return (group != null);
	}
	
	public Object[] getSelectedObjects() {
		if (state) {
			Object[] items = new Object[1];
			items[0] = label;
			return items;
		}
		
		return null;
	}
	
	public void setFont(Font f) {
		super.setFont(f);
		this.boxSize = f.getSize() + 2;
		super.setSize(getPreferredSize());
	}
	
	public void moveLabelUp(int upMover) {
		mover += upMover;
		repaint();
	}
	
	public void moveLabelDown(int downMover) {
		mover -= downMover;
		repaint();
	}
	
	public void addItemListener(ItemListener listener) {
		if (listener == null) {
			return;
		}
		itemListener = AWTEventMulticaster.add(itemListener, listener);
		super.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
	public void removeItemListener(ItemListener listener) {
		if (listener == null) {
			return;
		}
		itemListener = AWTEventMulticaster.remove(itemListener, listener);
	}
	
	public boolean isInside(int x, int y) {
		return (x < getSize().width && y < getSize().height);
	}
	
	public void update(Graphics g) {
		this.paint(g);
	}
	
	public void paint(Graphics g) {
		Color foreground = super.getForeground();
		if (!super.isEnabled()) {
			foreground = super.getBackground().darker().darker();
		}
		
		g.setColor(getBackground());
		g.fillRect(0, 0, boxSize, boxSize);
		
		g.setColor(foreground);
		g.setFont(super.getFont());
		g.drawString(label, boxSize + 4, getFont().getSize() - mover);
		
		if (isGrouped()) {
			g.drawOval(0, 0, boxSize - 1, boxSize - 1);
			
			if (state) {
				g.fillOval(3, 3, boxSize - 6, boxSize - 6);
			}
			
			if (pressed) {
				dragExit = true;
				g.drawOval(1, 1, boxSize - 3, boxSize - 3);
			}
		} else {
			g.drawRect(0, 0, boxSize - 1, boxSize - 1);
			
			if (state) {
				g.drawLine(0, 0, boxSize - 1, boxSize - 1);
				g.drawLine(boxSize - 1, 0, 0, boxSize - 1);
			}
			
			if (pressed) {
				dragExit = true;
				g.drawRect(1, 1, boxSize - 3, boxSize - 3);
			}
		}
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
			if (!isGrouped()) {
				state = !state;
			} else {
				group.setSelectedCheckbox(this);
			}
			pressed = false;
			repaint(); 
			
			if (itemListener != null) {
				itemListener.itemStateChanged(
					new ItemEvent(
						this,
						ItemEvent.ITEM_STATE_CHANGED,
						label,
						(state) ? ItemEvent.SELECTED : ItemEvent.DESELECTED
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
