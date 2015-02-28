// ===========================================================================
//	NumbersView.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.DecHexConverter;

/**
 *	<code>NumbersView</code>
 *
 *	@version	1.40f2
 *	@see		douglas.mencken.beans.TextView
 */

public class NumbersView extends Component implements MouseListener {
	
	protected TextView linkView;
	private boolean hex;
	
	private int leading;
	private int[] values;
	private int maxValue;
	
	private boolean dec2hexSpace = false;
	private String[] hexValues;
	private Image offscreenImage;
	private Graphics offscreenGraphics;
	private boolean useBorder;
	
	public NumbersView(TextView linkView) {
		this(linkView, Color.black, Color.lightGray);
	}
	
	public NumbersView(TextView linkView, Color fgColor, Color bgColor) {
		this.values = null;
		
		setForeground(fgColor);
		setBackground(bgColor);
		setFont(linkView.getFont());
		
		this.linkView = linkView;
		this.leading = linkView.getLeading();
		linkView.setNumbersView(this);
		
		this.maxValue = 100000;
		addMouseListener(this);
	}
	
	public NumbersView(TextView linkView, int[] values) {
		this(linkView, Color.black, Color.lightGray, values);
	}
	
	public NumbersView(TextView linkView, Color fgColor, Color bgColor, int[] values) {
		if ((values == null) || (values.length == 0)) {
			throw new IllegalArgumentException(
				"douglas.mencken.beans.NumbersView.<init>: 'int[] values' cannot be empty or null"
			);
		}
		
		this.values = values;
		this.maxValue = ArrayUtilities.getMaxElement(values);
		this.hexValues = getHexValues(values);
		
		if (Integer.toString(this.maxValue).length() >
				DecHexConverter.DECtoHEX(this.maxValue).length()) {
			this.dec2hexSpace = true;
		}
		
		setForeground(fgColor);
		setBackground(bgColor);
		setFont(linkView.getFont());
		
		this.leading = linkView.getLeading();
		linkView.setNumbersView(this);
		this.linkView = linkView;
	}
	
	public void useBorder() { useBorder = true; }
	
	/**
	 *	@return		a value found in list at 'index'
	 */
	public int getValueByIndex(int index) {
		if (this.values != null) {
			if ((index >= 0) && (index < this.values.length)) {
				return this.values[index];
			}
		}
		
		return -1;
	}
	
	/**
	 *	@return		an index of 'value' in list, -1 if not found
	 */
	public int getIndexByValue(int value) {
		int count = this.values.length;
		for (int index = 0; index < count; index++) {
			if (this.values[index] == value) {
				return index;
			}
		}
		
		return -1;
	}
	
	private String[] getHexValues(int[] values) {
		int len = values.length;
		String[] hexes = new String[len];
		
		for (int i = 0; i < len; i++) {
			hexes[i] = DecHexConverter.DECtoHEX(values[i]);
		}
		
		return hexes;
	}
	
	public Dimension getPreferredSize() {
		int width, height;
		
		if (this.values != null) {
			int linesCount = values.length;
			height = 4 + getFont().getSize()*linesCount + leading*linesCount;
		} else {
			height = getToolkit().getScreenSize().height;
		}
		width = 8 + getFontMetrics(getFont()).stringWidth(String.valueOf(this.maxValue));
		
		return new Dimension(width, height);
	}
	
	/**
	 *	Returns true because this component is painted to an offscreen image
	 *	("buffer") that's copied to the screen later.
	 */
	public boolean isDoubleBuffered() {
		return true;
	}
	
	public void update(Graphics g) {
		if (offscreenImage == null) {
			offscreenImage = createImage(getSize().width, getSize().height);
			offscreenGraphics = offscreenImage.getGraphics();
		}
		
		this.paint(offscreenGraphics);
		g.drawImage(offscreenImage, 0, 0, this);
	}
	
	// Note that to avoid deadlocks, paint is *not* synchronized
	public void paint(Graphics g) {
		Rectangle r = g.getClipBounds();
		
		g.setFont(super.getFont());
		
		g.setColor(super.getBackground());
		g.fillRect(0, 0, r.width, r.height);
		g.setColor(super.getForeground());
		
		int fontSize = super.getFont().getSize();
		int pos = fontSize + leading;
		
		if (this.values != null) {
			int maxLength = (Integer.toString(this.maxValue)).length();
			int maxHexLength = (DecHexConverter.DECtoHEX(this.maxValue)).length();
			if (dec2hexSpace) maxHexLength++;
			
			for (int i = linkView.start; i < linkView.end; i++) {
				if (i < values.length) {
					if (hex) {
						String number = StringUtilities.toPretabbedString(
							hexValues[i], maxHexLength
						);
						g.drawString(number, 4, pos);
					} else {
						String number = StringUtilities.toPretabbedString(values[i], maxLength);
						g.drawString(number, 4, pos);
					}
					
					pos += (fontSize + leading);
				}
			}
		} else {
			int start = linkView.start + 1;
			int end = start + linkView.page + 1;
			
			for (int i = start; i < end; i++) {
				if (hex) {
					String number = StringUtilities.toPretabbedString(
						 DecHexConverter.DECtoHEX(i), 6
					);
					g.drawString(number, 4, pos);
				} else {
					String number = StringUtilities.toPretabbedString(
						 Integer.toString(i), 6
					);
					g.drawString(number, 4, pos);
				}
				
				pos += (fontSize + leading);
			}
		}
		
		g.setColor(getBackground().brighter());
		g.drawLine(0, 0, 0, r.height);
		g.setColor(getBackground().darker());
		g.drawLine(r.width-2, 0, r.width-2, r.height);
		g.setColor(getForeground());
		g.drawLine(r.width-1, 0, r.width-1, r.height);
		
		if (useBorder) {
			g.setColor(Color.black);
			g.drawRect(0, 0, r.width-1, r.height-1);
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    
	public void mouseClicked(MouseEvent e) {
		if (linkView != null) {
			// if (linkView instanceof TextViewSelectableLine) {
			// 	linkView.mouseClicked(e);
			// }
			
			linkView.requestFocus();
		}
	}
	
	public int getLeading() { return leading; }
	public boolean getHexEnabled() { return hex; }
	
	public void setHexMode(boolean newHex) {
		if (hex != newHex) {
			hex = newHex;
			repaint();
		}
	}
	
}