/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.icons;

/**
 *	<code>EmptyIcon</code>
 *
 *	@version	1.0
 */

public final class EmptyIcon extends Icon {

	public int iconSize;

	public EmptyIcon() {
		this(32);
	}

	public EmptyIcon(int size) {
		this.iconSize = size;

		IconMaker im = new IconMaker();
		im.setDimensions(size, size);

		StringBuffer spaces = new StringBuffer();
		for (int i = 0; i < size; i++) {
			spaces.append(" ");
		}
		String filler = spaces.toString();
		for (int i = 0; i < size; i++) {
			im.setPixels(filler);
		}

		iconName = "EmptyIcon";
		image = im.createImage(this);
	}

	public int getIconSize() { return this.iconSize; }

}
