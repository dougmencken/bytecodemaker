/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.util.StringUtilities;

/**
 *	<code>TextView</code>
 *
 *	@version	1.53f1
 *	@see		douglas.mencken.beans.NumbersView
 */

public class TextView extends Component implements FocusListener, MouseListener {
	
	protected StringBuffer buffer;
	// protected String[] strings;
	
	protected int leading;
	protected int start;
	protected int end;
	protected int page;
	protected int xpos = 0;
	
	private Image offscreenImage;
	private Graphics offscreenGraphics;
	protected boolean focus;
	protected boolean useBorder;
	
	protected NumbersView numbersView;
	
	/**
	 *	Default leading: <b>2</b> <br>
	 *	Default foreground: <b>black</b> <br>
	 *	Default background: <b>white</b>
	 */
	public TextView() {
		this(2, Color.black, Color.white);
	}
	
	public TextView(int leading) {
		this(leading, Color.black, Color.white);
	}
	
	public TextView(Color fg, Color bg) {
		this(2, fg, bg);
	}
	
	public TextView(int leading, Color fg, Color bg) {
		super.enableEvents(AWTEvent.KEY_EVENT_MASK);
		
		this.leading = leading;
		super.setForeground(fg);
		super.setBackground(bg);
		
		super.setFont(new Font("Geneva", Font.PLAIN, 10));
		this.buffer = new StringBuffer();
		// this.strings = new String[0];
		
		super.addFocusListener(this);
		super.addMouseListener(this);
	}
	
	public void useBorder() { useBorder = true; }
	
	public void invalidate() {
		this.updateValues();
		super.invalidate();
	}
	
	protected void updateValues() {
		int vCount = getSize().height/(getFont().getSize() + this.leading);
		this.end = start + vCount;
		this.page = vCount - 1;
	}
	
	public void setFont(Font f) {
		super.setFont(f);
		
		if (numbersView != null) {
			numbersView.setFont(f);
		}
	}
	
	public void setNumbersView(NumbersView numbersView) { this.numbersView = numbersView; }
	public NumbersView getNumbersView() { return this.numbersView; }
	
	public void append(String what) {
		if (what != null) {
			if (!what.equals("")) {
				this.buffer.append(what);
				// this.strings = StringUtilities.extractStrings(this.buffer);
				this.updateValues();
				this.repaint();
			}
		}
	}
	
	public void append(char what) {
		this.buffer.append(what);
		// this.strings = StringUtilities.extractStrings(this.buffer);
		this.updateValues();
		this.repaint();
	}
	
	public void append(Object what) {
		if (what != null) {
			this.buffer.append(what.toString());
			// this.strings = StringUtilities.extractStrings(this.buffer);
			this.updateValues();
			this.repaint();
		}
	}
	
	public void append(int what) {
		this.buffer.append(Integer.toString(what));
		// this.strings = StringUtilities.extractStrings(this.buffer);
		this.updateValues();
		this.repaint();
	}
	
	public void appendln(String what) {
		if (what != null) {
			this.buffer.append(what).append('\n');
			// this.strings = StringUtilities.extractStrings(this.buffer);
			this.updateValues();
			this.repaint();
		}
	}
	
	public void appendln(char what) {
		this.buffer.append(what).append('\n');
		// this.strings = StringUtilities.extractStrings(this.buffer);
		this.updateValues();
		this.repaint();
	}
	
	public void appendln(Object what) {
		if (what != null) {
			this.buffer.append(what.toString()).append('\n');
			// this.strings = StringUtilities.extractStrings(this.buffer);
			this.updateValues();
			this.repaint();
		}
	}
	
	public void appendln(int what) {
		this.buffer.append(Integer.toString(what)).append('\n');
		// this.strings = StringUtilities.extractStrings(this.buffer);
		this.updateValues();
		this.repaint();
	}
	
	public void clear() {
		this.buffer.setLength(0);
		// this.strings = new String[0];
		this.repaint();
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		this.invalidate();
	}
	
	public Dimension getPreferredSize() {
		int linesCount = StringUtilities.countLines(this.buffer);
		int height = 4 + (getFont().getSize() + this.leading)*linesCount;
		int width = (int)(getToolkit().getScreenSize().width / 1.5f);
		
		return new Dimension(width, height);
	}
	
	/**
	 * Returns true because this component is painted to an offscreen image
	 * ("buffer") that's copied to the screen later.
	 */
	public boolean isDoubleBuffered() {
		return true;
	}
	
	public void repaint() {
		if (numbersView != null) numbersView.repaint();
		super.repaint();
	}
	
	public void update(Graphics g) {
		if (offscreenImage == null) {
			offscreenImage = createImage(getSize().width, getSize().height);
			offscreenGraphics = offscreenImage.getGraphics();
		}
		
		paint(offscreenGraphics);
		g.drawImage(offscreenImage, 0, 0, this);
	}
	
	// Note that to avoid deadlocks, paint is *not* synchronized
	public void paint(Graphics g) {
		if (this.buffer.length() == 0) return;
		
		g.setFont(getFont());
		g.setColor(getBackground());
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(getForeground());
		
		int fontSize = getFont().getSize();
		int pos = fontSize + this.leading;
		
		int stringCount = StringUtilities.countLines(this.buffer);
		if (end > stringCount) {
			start -= (end - stringCount);
			end = stringCount;
		}
		if (start < 0) start = 0;
		
		for (int i = start; i < end; i++) {
			try {
				g.drawString(
					(StringUtilities.getLineNumber(this.buffer, i)).substring(xpos),
					4, pos
				); 
			} catch (IndexOutOfBoundsException ignored) {}
			pos += fontSize + this.leading;
		}
		
		if (focus || useBorder) {
			g.setColor(Color.black);
			g.drawRect(0, 0, getSize().width-1, getSize().height-1);
		}
	}
	
	public void focusGained(FocusEvent evt) {
		this.focus = true;
		this.repaint();
	}
	
	public void focusLost(FocusEvent evt) {
		this.focus = false;
		this.repaint();
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseClicked(MouseEvent evt) {
		super.requestFocus();
	}
	
	public void processKeyEvent(KeyEvent evt) {
		if (evt.getID() == KeyEvent.KEY_PRESSED) {
			int stringCount = StringUtilities.countLines(this.buffer);
			
			switch (evt.getKeyCode()) {
				case KeyEvent.VK_UP:
					if (start > 0) {
						start--;
						end--;
						this.repaint();
					}
					break;
				case KeyEvent.VK_DOWN:
					if (end < stringCount) {
						start++;
						end++;
						this.repaint();
					}
					break;
				case KeyEvent.VK_LEFT:
					if (xpos >= 1) {
						xpos--;
						this.repaint();
					}
					break;
				case KeyEvent.VK_RIGHT:
					xpos++;
					this.repaint();
					break;
				case KeyEvent.VK_HOME:
					this.goTop();
					break;
				case KeyEvent.VK_END:
					this.goBottom();
					break;
				case KeyEvent.VK_PAGE_UP:
					if (start > 0) {
						start -= page;
						end -= page;
						if (start < 0) {
							end -= start;
							start = 0;
						}
						this.repaint();
					}
					break;
				case KeyEvent.VK_PAGE_DOWN:
					if (end < stringCount) {
						start += page;
						end += page;
						if (end > stringCount) {
							start -= (end - stringCount);
							end = stringCount;
						}
						this.repaint();
					}
					break;
			}
		}
	}
	
	public void goTop() {
		if (start != 0) {
			end -= start;
			start = 0;
			this.repaint();
		}
	}
	
	public void goBottom() {
		int stringCount = StringUtilities.countLines(this.buffer);
		if (end != stringCount) {
			start += stringCount - end;
			end = stringCount;
			this.repaint();
		}
	}
	
	public int getLeading() { return this.leading; }
	public String getContents() { return this.buffer.toString(); }
	
}