/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.icons;

/**
 *	<code>FloppyIcon</code>
 *
 *	@version	1.0
 */

public class FloppyIcon extends Icon {
	
	public FloppyIcon() {
		IconMaker im = new IconMaker();
		im.setDimensions(16, 16);
		
		im.setColor('o', 0xff999999);
		im.setColor('-', 0xffffffff);
		im.setColor('B', 0xff000000);
		
		im.setPixels("BBBBBBBBBBBBBBBB");
		im.setPixels("BoB----------B-B");
		im.setPixels("BoBooooooooooBBB");
		im.setPixels("BoB----------BoB");
		im.setPixels("BoBooooooooooBoB");
		im.setPixels("BoB----------BoB");
		im.setPixels("BoBooooooooooBoB");
		im.setPixels("BoB----------BoB");
		im.setPixels("BooBBBBBBBBBBooB");
		im.setPixels("BooooooooooooooB");
		im.setPixels("BooBBBBBBBBBoooB");
		im.setPixels("BooBBBBBB---BooB");
		im.setPixels("BooBBBBBB---BooB");
		im.setPixels("BooBBBBBB---BooB");
		im.setPixels("BooBBBBBB---BooB");
		im.setPixels(" BBBBBBBBBBBBBBB");
		
		iconName = "FloppyIcon";
		image = im.createImage(this);
	}
	
}