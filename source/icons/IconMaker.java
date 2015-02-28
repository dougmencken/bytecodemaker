// ===========================================================================
//	IconMaker.java (part of douglas.mencken.icons package)
// ===========================================================================
// @DEPRECATED
// Replaced by douglas.mencken.gui.ImageMaker
// ===========================================================================

package douglas.mencken.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;

/**
 *	<code>IconMaker</code>
 *
 *	@version	1.06f1
 */

// @DEPRECATED
// Replaced by douglas.mencken.gui.ImageMaker

public class IconMaker extends Object {
	
	protected int width = 0;
	protected int height = 0;
	
	/**
	 *	Row to be filled by default.
	 */
	protected int currentRow = 0;
	
	/**
	 *	The image itself.
	 */
	protected int[] pixels = null;
	
	/**
	 *	256 colors (one for each 1-byte symbol) are supported here.
	 */
	protected int[] colorMap;
	
	public IconMaker() {
		this.colorMap = new int[256];
		
		// Default colors
		this.colorMap[32] = 0x00000000;	// space
		this.colorMap[66] = 0xFF0000FF;	// 'B', blue
		this.colorMap[71] = 0xFF00FF00;	// 'G', green
		this.colorMap[75] = 0xFF000000;	// 'K', black
		this.colorMap[82] = 0xFFFF0000;	// 'R', red
		this.colorMap[87] = 0xFFFFFFFF;	// 'W', white
	}
	
	public synchronized void clear() {
		if ((width != 0) && (height != 0)) {
			this.pixels = new int[width*height];
		}
		
		this.currentRow = 0;
	}
	
	public synchronized Image createImage(Component c) {
		if (this.pixels != null) {
			MemoryImageSource imageSource = new MemoryImageSource(
				width, height, pixels, 0, width
			);
			return c.createImage(imageSource);
		}
		
		return null;
	}
	
	public void setColor(char letter, Color color) {
		this.setColor(letter, color.getRGB());
	}
	
	public synchronized void setColor(char letter, int rgb) {
		if (letter >= 256) return;
		this.colorMap[letter] = rgb;
	}
	
	public void setDimensions(Dimension d) {
		this.setDimensions(d.width, d.height);
	}
	
	public synchronized void setDimensions(int width, int height) {
		if ((this.width != width) && (this.height != height)) {
			this.width = width;
			this.height = height;
			
			if ((width != 0) && (height != 0)) {
				this.pixels = new int[width*height];
			} else {
				this.pixels = null;
			}
		}
	}
	
	/**
	 *	Sets pixels for the current row.
	 */
	public synchronized void setPixels(String p) {
		// height auto-increment: try to avoid it
		if (this.currentRow >= this.height) {
			Toolkit.getDefaultToolkit().beep();
			this.height++;
			
			int[] newPixels = new int[this.width * this.height];
			System.arraycopy(this.pixels, 0, newPixels, 0, this.pixels.length);
			this.pixels = newPixels;
		}
		
		this.setPixels(this.currentRow, p);
		this.currentRow++;
	}
	
	/**
	 *	Sets pixels for row specified by the first parameter.
	 */
	public synchronized void setPixels(int row, String p) {
		if (p.length() == this.width) {
			int offset = row * this.width;
			
			if (this.pixels == null) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			
			for (int i = 0; i < this.width; i++) {
				char c = p.charAt(i);
				if (c < 256) {
					this.pixels[i + offset] = this.colorMap[c];
				}
			}
		} else {
			throw new IllegalArgumentException(
				"Icon width (" + this.width +
				") and string length (" + p.length() +
				") mismatch"
			);
		}
	}
	
}

// @DEPRECATED
// Replaced by douglas.mencken.gui.ImageMaker

