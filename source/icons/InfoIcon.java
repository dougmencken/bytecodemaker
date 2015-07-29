/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.icons;

/**
 *	<code>InfoIcon</code>
 *
 *	@version	1.0
 */

public final class InfoIcon extends Icon {
	
	public InfoIcon() {
		IconMaker im = new IconMaker();
		im.setDimensions(32, 32);
		
		im.setColor('-', 0xffffffff);
		im.setColor('K', 0xff000000);
		im.setColor('G', 0xff808080);
		im.setColor('g', 0xffc0c0c0);
		im.setColor('B', 0xff0000ff);
		
		im.setPixels("           GGGGGGGG             ");
		im.setPixels("        GGGg------gGGG          ");
		im.setPixels("      GGg------------gGG        ");
		im.setPixels("     Gg----------------gG       ");
		im.setPixels("    G-------gBBBBg-------K      ");
		im.setPixels("   G--------BBBBBB--------K     ");
		im.setPixels("  G---------BBBBBB---------K    ");
		im.setPixels(" Gg---------gBBBBg---------gK   ");
		im.setPixels(" G--------------------------KG  ");
		im.setPixels("Gg--------------------------gKG ");
		im.setPixels("G----------BBBBBBB-----------KG ");
		im.setPixels("G------------BBBBB-----------KGG");
		im.setPixels("G------------BBBBB-----------KGG");
		im.setPixels("G------------BBBBB-----------KGG");
		im.setPixels("G------------BBBBB-----------KGG");
		im.setPixels("Gg-----------BBBBB----------gKGG");
		im.setPixels(" G-----------BBBBB----------KGGG");
		im.setPixels(" Gg----------BBBBB---------gKGGG");
		im.setPixels("  G--------BBBBBBBBB-------KGGG ");
		im.setPixels("   K----------------------KGGGG ");
		im.setPixels("    K--------------------KGGGG  ");
		im.setPixels("     Kg----------------gKGGGG   ");
		im.setPixels("      KKg------------gKKGGGG    ");
		im.setPixels("       GKKKg------gKKKGGGGG     ");
		im.setPixels("        GGGKKKg---KGGGGGGG      ");
		im.setPixels("          GGGGK---KGGGGG        ");
		im.setPixels("             GK---KGG           ");
		im.setPixels("               K--KGG           ");
		im.setPixels("                K-KGG           ");
		im.setPixels("                 KKGG           ");
		im.setPixels("                  GGG           ");
		im.setPixels("                   GG           ");
		
		iconName = "InfoIcon";
		image = im.createImage(this);
	}
	
}