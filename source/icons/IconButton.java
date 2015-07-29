/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.icons;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.icons.Icon;

/**
 *	<code>IconButton</code>
 *
 *	@version 1.2.4f
 */

public class IconButton extends Component implements MouseListener {

	private Icon icon;
	private ActionListener actionListener;
	private String actionCommand;
	
	private boolean pressed = false;
	private boolean dragExit = false;
	private int imagePos;
	
	public IconButton() {
		this(null, Color.lightGray);
	}
	
	public IconButton(Icon icon) {
		this(icon, Color.lightGray);
	}
	
	public IconButton(Icon icon, Color color) {
		this.icon = icon;
		super.setSize(getMinimumSize());
		
		setActionCommand((icon == null) ? "IconButton" : icon.getName());
		addMouseListener(this);
		
		setForeground(color);
		repaint();
	}
	
	public Icon getIcon() {
		return this.icon;
	}
	
	public void setIcon(Icon icon) {
		if (icon != this.icon) {
			this.icon = icon;
			repaint();
		}
	}
	
	public String getActionCommand() {
		return this.actionCommand;
	}
	
	public void setActionCommand(String command) {
		this.actionCommand = command;
	}
	
	public Dimension getMinimumSize() {
		if (icon != null) {
			return new Dimension(
				icon.getImage().getWidth(this) + 8,
				icon.getImage().getHeight(this) + 8
			);
		} else {
			return new Dimension(8, 8);
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(getSize());
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		
		if (icon != null) {
			if ((width >= getMinimumSize().width) &&
				  (height >= getMinimumSize().height)) {
				imagePos = (int) ((width - icon.getImage().getWidth(this)) / 2);
			}
		}
	}
	
	public void addActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.add(actionListener, listener);
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
	public void removeActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public void mousePressed(MouseEvent e) {
		pressed = true;
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		dragExit = false;
		
		if (pressed) {
			pressed = false;
			repaint(); 
			
			if (actionListener != null)
				actionListener.actionPerformed(
					new ActionEvent(
						this,
						ActionEvent.ACTION_PERFORMED,
						actionCommand
					)
				);
		}
	}
	
	public void mouseEntered(MouseEvent e) {
		if (dragExit) {
			pressed = true;
			repaint();
		}
	}
	
	public void mouseExited(MouseEvent e) {
		if (pressed == true) {
			pressed = false;
			repaint();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void update(Graphics g) {
		this.paint(g);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Dimension r = getSize();
		
		g.setColor(getBackground());
		g.drawLine(0, 0, 0, 0);
		g.drawLine(r.width-1, 0, r.width-1, 0);
		g.drawLine(0, r.height-1, 0, r.height-1);
		g.drawLine(r.width-1, r.height-1, r.width-1, r.height-1);
		
		if (!pressed) {
			g.drawLine(1, r.height-2, 1, 1);
			g.drawLine(1, 1, r.width-2, 1);
		}
		
		g.setColor(Color.black);
		g.drawLine(1, 0, r.width-2, 0);
		g.drawLine(0, 1, 0, r.height-2);
		g.drawLine(1, r.height-1, r.width-2, r.height-1);
		g.drawLine(r.width-1, 1, r.width-1, r.height-2);
		
		g.setColor(getForeground());
		g.fillRect(2, 2, r.width-3, r.height-3);
		
		g.setColor(getForeground().darker());
		if (pressed) {
			dragExit = true;
			
			if (icon != null) {
				g.drawImage(icon.getImage(), imagePos+1, imagePos+1, this);
			}
			
			g.drawLine(1, r.height-2, 1, 1);
			g.drawLine(2, r.height-3, 2, 2);
			g.drawLine(1, 1, r.width-2, 1);
			g.drawLine(2, 2, r.width-3, 2);
			g.drawLine(r.width-2, 1, r.width-2, r.height-2);
			g.drawLine(r.width-2, r.height-2, 1, r.height-2);
		} else {
			if (icon != null) {
				g.drawImage(icon.getImage(), imagePos, imagePos, this);
			}
			
			g.drawLine(r.width-2, 2, r.width-2, r.height-2);
			g.drawLine(r.width-3, 2, r.width-3, r.height-3);
			g.drawLine(r.width-2, r.height-2, 2, r.height-2);
			g.drawLine(r.width-3, r.height-3, 2, r.height-3);
		}
	}
	
}