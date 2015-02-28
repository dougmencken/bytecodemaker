// ===========================================================================
//	HideButton.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.icons.IconMaker;

/**
 *	<code>HideButton</code>
 *
 *	@version 1.0f
 */

public class HideButton extends Component implements MouseListener {
	
	public static final boolean HIDE_LABEL = false;
	public static final boolean CLOSE_LABEL = true;
	
	private boolean label;
	private Image hideB;
	private Image hideBPressed;
	
	private boolean pressed = false;
	private boolean dragExit = false;
	private ActionListener actionListener;
	private String actionCommand;
	
	public HideButton(boolean whichLabel) {
		this.label = whichLabel;
		setFont(new Font("Geneva", Font.PLAIN, 10));
		FontMetrics fm = getFontMetrics(getFont());
		
		int width = 14;
		if (whichLabel == HIDE_LABEL) {
			width += fm.stringWidth("Hide");
			actionCommand = "Hide";
		} else {
			width += fm.stringWidth("Close");
			actionCommand = "Close";
		}
		
		addMouseListener(this);
		
		createImages();
		super.setSize(width, 10);
		repaint();
	}
	
	public HideButton() {
		this(HIDE_LABEL);
	}
	
	public Dimension getMinimumSize() { return new Dimension(10, 10); }
	public Dimension getPreferredSize() { return new Dimension(getBounds().width, 10); }
	
	// You cannot change the height of 'HideButton'
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, 10);
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
			g.drawImage(hideBPressed, 1, 1, this);
		} else {
			g.drawImage(hideB, 1, 1, this);
		}
		
		String l = "Hide";
		if (label == CLOSE_LABEL) {
			l = "Close";
		}
		
		g.setFont(getFont());
		g.drawString(l, 14, 9);
	}
	
	private void createImages() {
		IconMaker im = new IconMaker();
		im.setDimensions(8, 8);
		
		im.setColor('-', 0xffbbbbbb);
		im.setColor('A', 0xff333366);
		im.setColor('1', 0xffccccff);
		
		im.setPixels("AAAAAAAA");
		im.setPixels("A1111111");
		im.setPixels("A1----A1");
		im.setPixels("A1----A1");
		im.setPixels("A1----A1");
		im.setPixels("A1----A1");
		im.setPixels("A1AAAAA1");
		im.setPixels("A1111111");
		
		hideB = im.createImage(this);
		im.clear();
		
		im.setColor('X', 0xff000000);
		im.setColor('-', 0xff9999cc);
		
		im.setPixels("XXXXXXXX");
		im.setPixels("XX----XX");
		im.setPixels("X-X--X-X");
		im.setPixels("X--XX--X");
		im.setPixels("X--XX--X");
		im.setPixels("X-X--X-X");
		im.setPixels("XX----XX");
		im.setPixels("XXXXXXXX");
		
		hideBPressed = im.createImage(this);
	}
	
}