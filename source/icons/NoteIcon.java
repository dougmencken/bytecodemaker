// ===========================================================================
//	NoteIcon.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

/**
 *	<code>NoteIcon</code>
 *
 *	@version	1.0
 */

public final class NoteIcon extends Icon {
	
	public NoteIcon() {
		IconMaker im = new IconMaker();
		im.setDimensions(32, 32);
		
		im.setColor('w', 0xffffffff);
		im.setColor('K', 0xff000000);
		im.setColor('-', 0xff222222);
		im.setColor('G', 0xff444444);
		im.setColor('B', 0xff333366);
		im.setColor('b', 0xff6666cc);
		im.setColor('l', 0xff9999ff);
		im.setColor('v', 0xffccccff);
		im.setColor('s', 0xffffcc99);
		im.setColor('S', 0xffcc9966);
		
		im.setPixels("--------------------------------");
		im.setPixels("-wwwwwwwsGGGGGGGGGGGGGGGGGGGGGG-");
		im.setPixels("-wssssssS----------------------K");
		im.setPixels("-wssssssS----------------------K");
		im.setPixels("-wssssssS----------------------K");
		im.setPixels("-wsSSsssS--------bvvvvvlBB-----K");
		im.setPixels("-wsSKwssS------bvwwwwwwvllB----K");
		im.setPixels("-wsSKwssS-----bvwwvvvvvvvlbB---K");
		im.setPixels("-wsSKwssS----bvwwvvvvvvvvvlbB--K");
		im.setPixels("-wsswwssS----vwwvvvvvvvvvvvlb--K");
		im.setPixels("-wssssssS---bwwvvvvvvvvvvvvvbB-K");
		im.setPixels("-wssssssS---vwwvvvvvvvvvvvvvlb-K");
		im.setPixels("-wssssssS---vwvKKKvKKKvKKKvvlb-K");
		im.setPixels("-wssssssS---vwvvvvvvvvvvvvvvlb-K");
		im.setPixels("-wssssssS---vwvvvvvvvvvvvvvvlb-K");
		im.setPixels("-wssssssS---vwvKKKvKKKvKvKvvlb-K");
		im.setPixels("-wssssssS---vwvvvvvvvvvvvvvvlb-K");
		im.setPixels("-wssssssS---vwvvvvvvvvvvvvvvlb-K");
		im.setPixels("-wssssssS---vwvKKKvKvKKKvvvvlb-K");
		im.setPixels("-wsssSSSS---vwvvvvvvvvvvvvvvlb-K");
		im.setPixels("-wsss-------vwvvvvvvvvvvvvvvlb-K");
		im.setPixels("-wssssS-----vwvKKKKvKKKvKKvvlb-K");
		im.setPixels("-wssssS-----vwvvvvvvvvvvvvvllB-K");
		im.setPixels("-wssssS-----vwvvvvvvvvvvvvllb--K");
		im.setPixels("-wssssS----bvwvvvvvvvvvvvllbB--K");
		im.setPixels("-wssSSS---bvvllllllllllllbbB---K");
		im.setPixels("-wss-----bbbbbbbbbbbbbbbBBB----K");
		im.setPixels("-wssssS------------------------K");
		im.setPixels("-wssssS------------------------K");
		im.setPixels("-wssssS------------------------K");
		im.setPixels("-sSSSSS------------------------K");
		im.setPixels("-KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
		
		iconName = "NoteIcon";
		image = im.createImage(this);
	}
	
}