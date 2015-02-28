// ===========================================================================
//	IconListEditor.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.beans.*;
import java.awt.*;

/**
 *	<code>IconListEditor</code>
 *
 *	@version	1.10f3
 */

public class IconListEditor extends PropertyEditorSupport {
	
	private transient Icon icon = null;
	
	public String[] getTags() {
		return new String[] {
			"none",
			"CautionIcon",
			"NoteIcon",
			"PalmStopIcon",
			"QuestionIcon",
			"InfoIcon",
			"StopIcon",
			"FloppyIcon",
			"OpenIcon",
			"SaveIcon",
			"UpArrowIcon",
			"DownArrowIcon",
			"LeftArrowIcon",
			"RightArrowIcon",
			"CleaningUpIcon"
		};
	}
	
	public String getAsText() {
		if (icon != null) {
			return icon.getName();
		} else {
			return "none";
		}
	}
	
	public void setAsText(String text) throws IllegalArgumentException {
		if (!text.equals("none")) {
			try {
				Class cls = Class.forName("douglas.mencken.icons." + text);
				this.icon = (Icon)(cls.newInstance());
			} catch (Exception exc) {
				throw new IllegalArgumentException(
					"caught " + exc.getClass().getName()
				);
			}
		} else {
			this.icon = null;
		}
		
		super.firePropertyChange();
	}
	
	public Object getValue() {
		return this.icon;
	}
	
	public void setValue(Object newValue) {
		Icon newIcon = null;
		try {
			newIcon = (Icon)newValue;
		} catch (Exception e) {}
		
		this.icon = newIcon;
	}
	
}