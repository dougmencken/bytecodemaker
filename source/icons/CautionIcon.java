/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.icons;

/**
 *	<code>CautionIcon</code>
 *
 *	@version	1.0
 */

public final class CautionIcon extends Icon {

	public CautionIcon() {
		IconMaker im = new IconMaker();
		im.setDimensions(32, 32);
		
		im.setColor('-', 0xffffff00);
		im.setColor('K', 0xff000000);
		im.setColor('w', 0xffffffff);
		im.setColor('b', 0xffcc9966);
		im.setColor('g', 0xff888888);
		im.setColor('G', 0xff555555);
		
		im.setPixels("               KK               ");
		im.setPixels("              KKKK              ");
		im.setPixels("              KggK              ");
		im.setPixels("             KK-wKK             ");
		im.setPixels("             Kg-wgK             ");
		im.setPixels("            KK-w-bKK            ");
		im.setPixels("            Kg-w--gK            ");
		im.setPixels("           KK-w---bKK           ");
		im.setPixels("           Kg-w----gK           ");
		im.setPixels("          KK-w-----bKK          ");
		im.setPixels("          Kg-wgGGg--gK          ");
		im.setPixels("         KK-w-GKKG--bKK         ");
		im.setPixels("         Kg-w-KKKK---gK         ");
		im.setPixels("        KK-w--KKKK---bKK        ");
		im.setPixels("        Kg-w--KKKK----gK        ");
		im.setPixels("       KK-w---KKKK----bKK       ");
		im.setPixels("       Kg-w---KKKK-----gK       ");
		im.setPixels("      KK-w----KKKK-----bKK      ");
		im.setPixels("      Kg-w----KKKK------gK      ");
		im.setPixels("     KK-w-----GKKG------bKK     ");
		im.setPixels("     Kg-w-----gKKg-------gK     ");
		im.setPixels("    KK-w-------GG--------bKK    ");
		im.setPixels("    Kg-w------------------gK    ");
		im.setPixels("   KK-w-------------------bKK   ");
		im.setPixels("   Kg-w-------gKKg---------gK   ");
		im.setPixels("  KK-w--------KKKK---------bKK  ");
		im.setPixels("  Kg-w--------KKKK----------gK  ");
		im.setPixels(" KK-w---------gKKg----------bKK ");
		im.setPixels(" Kg-w------------------------gK ");
		im.setPixels("KKbbbbbbbbbbbbbbbbbbbbbbbbbbbbKK");
		im.setPixels("KKGGGGGGGGGGGGGGGGGGGGGGGGGGGGKK");
		im.setPixels(" KKKKKKKKKKKKKKKKKKKKKKKKKKKKKK ");
		
		iconName = "CautionIcon";
		image = im.createImage(this);
	}
	
}