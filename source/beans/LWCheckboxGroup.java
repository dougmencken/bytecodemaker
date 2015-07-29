/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.event.*;

/*
 * @version 1.2
 */

public class LWCheckboxGroup extends Object {
	
	LWCheckbox selectedCheckbox = null;
	
	public LWCheckboxGroup() {}
	
	public void setSelectedCheckbox(LWCheckbox box) {
		if (box != null && box.group != this) {
			return;
		}
		
		LWCheckbox oldChoice = this.selectedCheckbox;
		this.selectedCheckbox = box;
		if ((oldChoice != null) && (oldChoice != box)) {
			oldChoice.setState(false);
		}
		if (box != null && oldChoice != box && !box.getState()) {
			box.setState(true);
		}
	}
	
	public LWCheckbox getSelectedCheckbox() { return selectedCheckbox; }
	
	/**
	 * Returns a string representation of this check box group,
	 * including the value of its current selection.
	 */
	public String toString() {
		return getClass().getName() + "[selectedCheckbox=" + selectedCheckbox + "]";
    }
    
}