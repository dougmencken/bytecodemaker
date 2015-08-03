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
 *	<code>IconListEditor</code>
 *
 *	@version	1.2
 */

public class IconListEditor extends PropertyEditorSupport {
	
	private transient Icon icon;

	public IconListEditor() {
		super();
		this.icon = new EmptyIcon(16);
	}

	public String[] getTags() {
		return new String[] {
			"EmptyIcon",
			"NoteIcon",
			"PalmStopIcon",
			"CautionIcon",
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
		return (this.icon != null) ? this.icon.getName() : "";
	}
	
	public void setAsText(String text) throws IllegalArgumentException {
		if ((text != null) && (!text.equals(""))) {
			try {
				Class cls = Class.forName("douglas.mencken.icons." + text);
				this.icon = (Icon)(cls.newInstance());
			} catch (Exception exc) {
				throw new IllegalArgumentException(
					"caught " + exc.getClass().getName()
				);
			}
		} else {
			this.icon = new EmptyIcon(16);
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
