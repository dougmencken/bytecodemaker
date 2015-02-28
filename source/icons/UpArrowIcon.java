// ===========================================================================
//	UpArrowIcon.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.awt.Color;

/**
 *	<code>UpArrowIcon</code>
 *
 *	@version	1.0
 */

public final class UpArrowIcon extends Icon {
	
	public UpArrowIcon() {
		this(Color.black);
	}
	
	public UpArrowIcon(Color arrowColor) {
		IconMaker im = new IconMaker();
		im.setDimensions(15, 15);
		
		im.setColor('-', 0xffffffff);
		im.setColor('+', arrowColor.getRGB());
		
		im.setPixels("---------------");
		im.setPixels("-------+-------");
		im.setPixels("------+++------");
		im.setPixels("-----+++++-----");
		im.setPixels("----+++++++----");
		im.setPixels("---+++++++++---");
		im.setPixels("--+++-+++-+++--");
		im.setPixels("--++--+++--++--");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("---------------");
		
		super.iconName = "UpArrowIcon";
		super.image = im.createImage(this);
	}
	
}