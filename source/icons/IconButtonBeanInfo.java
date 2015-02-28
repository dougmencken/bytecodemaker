// ===========================================================================
//	IconButtonBeanInfo.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.beans.*;
import java.awt.*;

/**
 *	<code>IconButtonBeanInfo</code>
 *
 *	@version 1.0b0
 */

public class IconButtonBeanInfo extends SimpleBeanInfo {
	
	public Image getIcon(int iconKind) {
		switch (iconKind) {
			case ICON_COLOR_16x16:
				return loadImage("IconButton_Color16.gif");
			case ICON_MONO_16x16:
				return loadImage("IconButton_Mono16.gif");
				
			default:
				return null;
		}
	}
	
}