/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.icons;

import java.awt.Component;
import java.awt.Image;

/**
 *	<code>Icon</code>
 *
 *	@version 1.2
 */

public abstract class Icon extends Component {
	
	protected Image image;
	protected String iconName;
	
	public Image getImage() {
		return this.image;
	}
	
	public String getName() {
		return this.iconName;
	}
	
}
