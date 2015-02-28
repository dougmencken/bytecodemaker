// ===========================================================================
//	LWCheckboxGroup.java (part of douglas.mencken.beans package)
//		version 1.2.0
//	
// ===========================================================================

package douglas.mencken.beans;

import java.awt.event.*;

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