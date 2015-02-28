// ===========================================================================
//	LeftArrowIcon.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.awt.Color;

/**
 *	<code>LeftArrowIcon</code>
 *
 *	@version	1.0
 */

public final class LeftArrowIcon extends Icon {
	
	public LeftArrowIcon() {
		this(Color.black);
	}
	
	public LeftArrowIcon(Color arrowColor) {
		IconMaker im = new IconMaker();
		im.setDimensions(15, 15);
		
		im.setColor('-', 0xffffffff);
		im.setColor('+', arrowColor.getRGB());
		
		im.setPixels("---------------");
		im.setPixels("---------------");
		im.setPixels("------++-------");
		im.setPixels("-----+++-------");
		im.setPixels("----+++--------");
		im.setPixels("---+++---------");
		im.setPixels("--++++++++++++-");
		im.setPixels("-+++++++++++++-");
		im.setPixels("--++++++++++++-");
		im.setPixels("---+++---------");
		im.setPixels("----+++--------");
		im.setPixels("-----+++-------");
		im.setPixels("------++-------");
		im.setPixels("---------------");
		im.setPixels("---------------");
		
		super.iconName = "LeftArrowIcon";
		super.image = im.createImage(this);
	}
	
}