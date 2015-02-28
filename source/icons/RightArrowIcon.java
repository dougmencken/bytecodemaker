// ===========================================================================
//	RightArrowIcon.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.awt.Color;

/**
 *	<code>RightArrowIcon</code>
 *
 *	@version	1.0
 */

public final class RightArrowIcon extends Icon {
	
	public RightArrowIcon() {
		this(Color.black);
	}
	
	public RightArrowIcon(Color arrowColor) {
		IconMaker im = new IconMaker();
		im.setDimensions(15, 15);
		
		im.setColor('-', 0xffffffff);
		im.setColor('+', arrowColor.getRGB());
		
		im.setPixels("---------------");
		im.setPixels("---------------");
		im.setPixels("-------++------");
		im.setPixels("-------+++-----");
		im.setPixels("--------+++----");
		im.setPixels("---------+++---");
		im.setPixels("-++++++++++++--");
		im.setPixels("-+++++++++++++-");
		im.setPixels("-++++++++++++--");
		im.setPixels("---------+++---");
		im.setPixels("--------+++----");
		im.setPixels("-------+++-----");
		im.setPixels("-------++------");
		im.setPixels("---------------");
		im.setPixels("---------------");
		
		super.iconName = "RightArrowIcon";
		super.image = im.createImage(this);
	}
	
}