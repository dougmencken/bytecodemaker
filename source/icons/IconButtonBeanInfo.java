/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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