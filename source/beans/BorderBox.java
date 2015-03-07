// ===========================================================================
// BorderBox.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;

/**
 *	<code>BorderBox</code>
 *	
 *	@version 1.4
 */

public class BorderBox extends Container {
	
	public static final int BORDER_NONE = 0;
	public static final int BORDER_TOP = 1;
	public static final int BORDER_BOTTOM = 2;
	public static final int BORDER_LEFT = 4;
	public static final int BORDER_RIGHT = 8;
	public static final int BORDER_ALL =
				BORDER_TOP | BORDER_BOTTOM | BORDER_LEFT | BORDER_RIGHT;
	
	private int borderWidth;
	private int borderType;
	private boolean useShadow;
	
	public BorderBox() {
		this(new FlowLayout(FlowLayout.CENTER), 1, BORDER_ALL);
	}
	
	public BorderBox(int width) {
		this(new FlowLayout(FlowLayout.CENTER), width, BORDER_ALL);
	}
	
	public BorderBox(LayoutManager layout) {
		this(layout, 1, BORDER_ALL);
	}
	
	public BorderBox(LayoutManager layout, int width) {
		this(layout, width, BORDER_ALL);
	}
	
	public BorderBox(int width, int borderType) {
		this(new FlowLayout(FlowLayout.CENTER), width, borderType);
	}
	
	public BorderBox(LayoutManager layout, int width, int borderType) {
		super();
		super.setLayout(layout);
		
		if (width < 1) {
			throw new IllegalArgumentException("BorderBox: borderWidth can't be < 1");
		}
		
		this.borderWidth = width;
		this.borderType = borderType;
		this.useShadow = false;
		
		super.setForeground(Color.black);
		super.setBackground(Color.white);
	}
	
	public Component add(Component comp) {
		int x = comp.getLocation().x;
		int y = comp.getLocation().y;
		if (x < borderWidth) {
			comp.setLocation(borderWidth, y);
		}
		if (y < borderWidth) {
			comp.setLocation(x, borderWidth);
		}
		
		return super.add(comp);
	}
	
	public void setShadow(boolean useShadow) {
		this.useShadow = useShadow;
		super.repaint();
	}
	
	public boolean getShadow() {
		return this.useShadow;
	}
	
	public void setBorderWidth(int width) {
		this.borderWidth = width;
		super.repaint();
	}
	
	public int getBorderWidth() {
		return this.borderWidth;
	}
	
	public void setBorderType(int type) {
		this.borderType = type;
		super.repaint();
	}
	
	public int getBorderType() {
		return this.borderType;
	}
	
	public Dimension getPreferredSize() {
		Dimension preferredSize = super.getPreferredSize();
		int koef = 2;
		if (useShadow) koef = 3;
		
		return new Dimension(
			preferredSize.width + borderWidth*koef,
			preferredSize.height + borderWidth*koef
		);
	}
	
	public Insets getInsets() {
		return new Insets(borderWidth, borderWidth, borderWidth, borderWidth);
	}
	
	public void update(Graphics g) {
    	this.paint(g);
    }
    
	public void paint(Graphics g) {
		super.paint(g);
		
		int w = super.getSize().width;
		int h = super.getSize().height;
		
		g.setColor(super.getBackground());
		g.fillRect(borderWidth, borderWidth, w - borderWidth, h - borderWidth);
		g.setColor(super.getForeground());
		
		if ((borderType & BORDER_LEFT) != 0) {
			for (int i = 0; i < borderWidth; i++) {
				g.drawLine(i, 0, i, h-1);
			}
		}
		if ((borderType & BORDER_RIGHT) != 0) {
			for (int i = 1; i <= borderWidth; i++) {
				g.drawLine(w-i, 0, w-i, h-1);
			}
		}
		if ((borderType & BORDER_TOP) != 0) {
			for (int i = 0; i < borderWidth; i++) {
				g.drawLine(0, i, w-1, i);
			}
		}
		if ((borderType & BORDER_BOTTOM) != 0) {
			for (int i = 1; i <= borderWidth; i++) {
				g.drawLine(0, h-i, w-1, h-i);
			}
		}
		
		if (useShadow) {
			int borderWidth2 = borderWidth*2;
			g.setColor(super.getBackground().darker().darker());
			
			if ((borderType & BORDER_RIGHT) != 0) {
				for (int i = borderWidth + 1; i <= borderWidth2; i++) {
					g.drawLine(w-i, i-1, w-i, h-i);
				}
			}
			if ((borderType & BORDER_BOTTOM) != 0) {
				for (int i = borderWidth + 1; i <= borderWidth2; i++) {
					g.drawLine(i-1, h-i, w-i, h-i);
				}
			}
		}
	}
    
}
