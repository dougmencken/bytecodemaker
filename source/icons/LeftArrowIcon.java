/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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