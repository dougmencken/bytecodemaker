/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.icons;

/**
 *	<code>StopIcon</code>
 *
 *	@version 1.0
 */

public final class StopIcon extends Icon {
	
	public StopIcon() {
		IconMaker im = new IconMaker();
		im.setDimensions(32, 32);
		
		im.setColor('W', 0xffffffff);
		im.setColor('-', 0xffff0000);
		im.setColor('r', 0xff800000);
		im.setColor('G', 0xff808080);
		
		im.setPixels("           rrrrrrrr             ");
		im.setPixels("        rrr--------rrr          ");
		im.setPixels("       r--------------r         ");
		im.setPixels("     rr----------------rr       ");
		im.setPixels("    r--------------------r      ");
		im.setPixels("   r----------------------r     ");
		im.setPixels("   r----------------------rG    ");
		im.setPixels("  r------W----------W------rG   ");
		im.setPixels(" r------WWW--------WWW------r   ");
		im.setPixels(" r-----WWWWW------WWWWW-----rG  ");
		im.setPixels(" r------WWWWW----WWWWW------rGG ");
		im.setPixels("r--------WWWWW--WWWWW--------rG ");
		im.setPixels("r---------WWWWWWWWWW---------rG ");
		im.setPixels("r----------WWWWWWWW----------rGG");
		im.setPixels("r-----------WWWWWW-----------rGG");
		im.setPixels("r-----------WWWWWW-----------rGG");
		im.setPixels("r----------WWWWWWWW----------rGG");
		im.setPixels("r---------WWWWWWWWWW---------rGG");
		im.setPixels("r--------WWWWW--WWWWW--------rGG");
		im.setPixels(" r------WWWWW----WWWWW------rGGG");
		im.setPixels(" r-----WWWWW------WWWWW-----rGGG");
		im.setPixels(" r------WWW--------WWW------rGG ");
		im.setPixels("  r------W----------W------rGGG ");
		im.setPixels("   r----------------------rGGGG ");
		im.setPixels("   r----------------------rGGG  ");
		im.setPixels("    r--------------------rGGG   ");
		im.setPixels("     rr----------------rrGGGG   ");
		im.setPixels("      Gr--------------rGGGGG    ");
		im.setPixels("       Grrr--------rrrGGGGG     ");
		im.setPixels("         GGrrrrrrrrGGGGGG       ");
		im.setPixels("          GGGGGGGGGGGGGG        ");
		im.setPixels("             GGGGGGGG           ");
		
		iconName = "StopIcon";
		image = im.createImage(this);
	}
	
}