// ===========================================================================
//	DownArrowIcon.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.awt.Color;

/**
 *	<code>DownArrowIcon</code>
 *
 *	@version	1.0
 */

public final class DownArrowIcon extends Icon {
	
	public DownArrowIcon() {
		this(Color.black);
	}
	
	public DownArrowIcon(Color arrowColor) {
		IconMaker im = new IconMaker();
		im.setDimensions(15, 15);
		
		im.setColor('-', 0xffffffff);
		im.setColor('+', arrowColor.getRGB());
		
		im.setPixels("---------------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("------+++------");
		im.setPixels("--++--+++--++--");
		im.setPixels("--+++-+++-+++--");
		im.setPixels("---+++++++++---");
		im.setPixels("----+++++++----");
		im.setPixels("-----+++++-----");
		im.setPixels("------+++------");
		im.setPixels("-------+-------");
		im.setPixels("---------------");
		
		super.iconName = "DownArrowIcon";
		super.image = im.createImage(this);
	}
	
}