// ===========================================================================
//	PalmStopIcon.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

/**
 *	<code>PalmStopIcon</code>
 *
 *	@version 1.0
 */

public final class PalmStopIcon extends Icon {
	
	public PalmStopIcon() {
		IconMaker im = new IconMaker();
		im.setDimensions(32, 32);
		
		im.setColor('W', 0xffffffff);
		im.setColor('-', 0xffdd0000);
		im.setColor('v', 0xffff66cc);
		im.setColor('V', 0xff990066);
		im.setColor('y', 0xffffffcc);
		im.setColor('o', 0xffffcc99);
		im.setColor('r', 0xffcc9966);
		im.setColor('B', 0xff333366);
		
		im.setPixels("        ----------------        ");
		im.setPixels("       --vvvvvvvvvvvvvv-V       ");
		im.setPixels("      --v----------------V      ");
		im.setPixels("     --v------------------V     ");
		im.setPixels("    --v-------yr-----------V    ");
		im.setPixels("   --v--------or------------V   ");
		im.setPixels("  --v------yr-or-yr----------V  ");
		im.setPixels(" --v-------or-or-or-----------V ");
		im.setPixels("--v--------or-or-or-----------VB");
		im.setPixels("-v------yr-or-or-or-----------VB");
		im.setPixels("-v------or-or-or-or-----------VB");
		im.setPixels("-v------or-or-or-or-----------VB");
		im.setPixels("-v------or-or-or-or-----------VB");
		im.setPixels("-v------or-or-or-or-----------VB");
		im.setPixels("-v------or-oroyroyr----ro-----VB");
		im.setPixels("-v------oroyoooooor---ryr-----VB");
		im.setPixels("-v------oooooooooorr-ryrr-----VB");
		im.setPixels("-v------ooooooooooorroor------VB");
		im.setPixels("-v------oooooooooooooorr------VB");
		im.setPixels("-v------oooooooooooooor-------VB");
		im.setPixels("-v------ooooooooooooorr-------VB");
		im.setPixels("-v------roooooooooooor--------VB");
		im.setPixels("-v------rooooooooooorr--------VB");
		im.setPixels("-v-------ooooooooooor---------VB");
		im.setPixels("W--------rooooooooorr--------VBW");
		im.setPixels(" WV-------rooooooorr--------VBW ");
		im.setPixels("  WV-------rrrrrrrr--------VBW  ");
		im.setPixels("   WV---------------------VBW   ");
		im.setPixels("    WV-------------------VBW    ");
		im.setPixels("     WV-----------------VBW     ");
		im.setPixels("      WV-VVVVVVVVVVVVVVVBW      ");
		im.setPixels("       WVBBBBBBBBBBBBBBBW       ");
		
		iconName = "PalmStopIcon";
		image = im.createImage(this);
	}
	
}