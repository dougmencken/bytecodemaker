/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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