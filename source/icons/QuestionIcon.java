// ===========================================================================
//	QuestionIcon.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

/**
 *	<code>QuestionIcon</code>
 *
 *	@version 1.0
 */

public final class QuestionIcon extends Icon {
	
	public QuestionIcon() {
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
		im.setPixels("    G--------------------K      ");
		im.setPixels("   G-------gBBBBBBg-------K     ");
		im.setPixels("  G-------gBg--BBBBg-------K    ");
		im.setPixels(" Gg-------BB----BBBB-------gK   ");
		im.setPixels(" G--------BBBB--BBBB--------KG  ");
		im.setPixels("Gg--------BBBB-gBBBB--------gKG ");
		im.setPixels("G---------gBBg-BBBB----------KG ");
		im.setPixels("G-------------gBBB-----------KGG");
		im.setPixels("G-------------BBB------------KGG");
		im.setPixels("G-------------BBg------------KGG");
		im.setPixels("G-------------BB-------------KGG");
		im.setPixels("Gg--------------------------gKGG");
		im.setPixels(" G-----------gBBg-----------KGGG");
		im.setPixels(" Gg----------BBBB----------gKGGG");
		im.setPixels("  G----------BBBB----------KGGG ");
		im.setPixels("   K---------gBBg---------KGGGG ");
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
		
		iconName = "QuestionIcon";
		image = im.createImage(this);
	}
	
}