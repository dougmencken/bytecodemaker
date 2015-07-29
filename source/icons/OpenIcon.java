/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.icons;

/**
 *	<code>OpenIcon</code>
 *
 *	@version	1.0
 */

public class OpenIcon extends Icon {
	
	public OpenIcon() {
		IconMaker im = new IconMaker();
		im.setDimensions(16, 16);
		
		im.setColor('r', 0xffdd0000);
		im.setColor('-', 0xffffffff);
		im.setColor('y', 0xffffff00);
		im.setColor('B', 0xff000000);
		
		im.setPixels("           r    ");
		im.setPixels("          rrr   ");
		im.setPixels("         rrrrr  ");
		im.setPixels("        rrrrrrr ");
		im.setPixels("          rrr   ");
		im.setPixels("    BBB   rrr   ");
		im.setPixels("   By-yB        ");
		im.setPixels("  By-y-yBBBBBBB ");
		im.setPixels("  B-y-y-y-y-y-yB");
		im.setPixels(" BBBBBBBBBBB-y-B");
		im.setPixels("By-y-y-y-y-yB-yB");
		im.setPixels("B-y-y-y-y-y-By-B");
		im.setPixels(" B-y-y-y-y-y-ByB");
		im.setPixels(" By-y-y-y-y-yB-B");
		im.setPixels("  By-y-y-y-y-yBB");
		im.setPixels("  BBBBBBBBBBBBB ");
		
		iconName = "OpenIcon";
		image = im.createImage(this);
	}
	
}