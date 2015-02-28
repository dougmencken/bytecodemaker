// ===========================================================================
//	Icon.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.awt.Component;
import java.awt.Image;

/**
 *	<code>Icon</code>
 *
 *	@version 1.16f
 */

public class Icon extends Component {
	
	protected Image image;
	protected String iconName;
	
	public Icon() {
		this.image = null;
		this.iconName = "none";
	}
	
	public Icon(Icon original) {
		if (original != null) {
			this.image = original.image;
			this.iconName = original.iconName;
		} else {
			this.image = null;
			this.iconName = "none";
		}
	}
	
	public Icon(Image image) {
		this.image = image;
		this.iconName = "Icon";
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public String getName() {
		return this.iconName;
	}
	
}