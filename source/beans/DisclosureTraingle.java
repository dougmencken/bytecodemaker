// ===========================================================================
//	DisclosureTraingle.java (part of douglas.mencken.beans package)
//		version 1.21f
//	
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.icons.IconMaker;

public class DisclosureTraingle extends Component implements MouseListener {
	
	private Image traingleUp;
	private Image traingleRight;
	private Image traingleDown;
	
	private boolean state;
	private boolean pressed = false;
	private boolean dragExit = false;
	
	private transient ActionListener actionListener;
	private String actionCommand;
	
	public DisclosureTraingle() {
		this(false);
	}
	
	public DisclosureTraingle(boolean state) {
		this.state = state;
		super.setSize(11, 11);
		addMouseListener(this);
		createImages();
		repaint();
	}
	
	public boolean getState() { return state; }
	
	public void setState(boolean newState) {
		if (newState != state) {
			state = newState;
			repaint();
		}
	}
	
	public Dimension getMinimumSize() { return new Dimension(11, 11); }
	public Dimension getPreferredSize() { return new Dimension(11, 11); }
	public Dimension getMaximumSize() { return new Dimension(11, 11); }
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, 11, 11);
	}
	
	public String getActionCommand() { return actionCommand; }
	
	public void setActionCommand(String command) {
		this.actionCommand = command;
	}
	
	public void addActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.add(actionListener, listener);
		super.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
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
			state = !state;
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
	
	public void paint(Graphics g) {
		g.setColor(Color.black);
		
		if (pressed) {
			dragExit = true;
			g.drawImage(traingleRight, 2, 0, this);
		} else {
			if (state) {
				g.drawImage(traingleUp, 0, 2, this);
			} else {
				g.drawImage(traingleDown, 0, 3, this);
			}
		}
	}
	
	private void createImages() {
		IconMaker im = new IconMaker();
		im.setDimensions(6, 11);
		im.setColor('-', 0xff9999ff);
		im.setColor('B', 0xff000000);
		
		im.setPixels("B     ");
		im.setPixels("BB    ");
		im.setPixels("B-B   ");
		im.setPixels("B--B  ");
		im.setPixels("B---B ");
		im.setPixels("B----B");
		im.setPixels("B---B ");
		im.setPixels("B--B  ");
		im.setPixels("B-B   ");
		im.setPixels("BB    ");
		im.setPixels("B     ");
		
		traingleRight = im.createImage(this);
		im.clear();
		im.setDimensions(11, 6);
		
		im.setPixels("     B     ");
		im.setPixels("    B-B    ");
		im.setPixels("   B---B   ");
		im.setPixels("  B-----B  ");
		im.setPixels(" B-------B ");
		im.setPixels("BBBBBBBBBBB");
		
		traingleUp = im.createImage(this);
		im.clear();
		
		im.setPixels("BBBBBBBBBBB");
		im.setPixels(" B-------B ");
		im.setPixels("  B-----B  ");
		im.setPixels("   B---B   ");
		im.setPixels("    B-B    ");
		im.setPixels("     B     ");
		
		traingleDown = im.createImage(this);
	}
	
}