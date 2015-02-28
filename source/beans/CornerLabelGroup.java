// ===========================================================================
//	CornerLabelGroup.java (part of douglas.mencken.beans package)
//		version 1.0.1
//	
// ===========================================================================

package douglas.mencken.beans;

/**
 * The <code>CornerLabelGroup</code> class is used to group together 
 * a set of <code>CornerLabel</code> components.
 */

public class CornerLabelGroup extends Object {
	
	public static final int PANELS_VERTICAL = 1;
	public static final int PANELS_HORIZONTAL = 2;
	
	// private int labelLayout;
	// private CornerLabel[] labels;
	
	public CornerLabelGroup(CornerLabel[] labels) {
		this(labels, PANELS_VERTICAL);
	}
	
	public CornerLabelGroup(CornerLabel[] labels, int layout) {
		if ((layout < 1) || (layout > 2)) {
			throw new IllegalArgumentException();
		}
		// this.labelLayout = layout;
		
		int borders = BorderBox.BORDER_TOP | BorderBox.BORDER_LEFT | BorderBox.BORDER_RIGHT;
		if (layout == PANELS_HORIZONTAL) {
			borders = BorderBox.BORDER_LEFT | BorderBox.BORDER_TOP | BorderBox.BORDER_BOTTOM;
		}
		
		int len_m1 = labels.length - 1;
		for (int i = 0; i < len_m1; i++) {
			labels[i].setBorderType(borders);
		}
		labels[len_m1].setBorderType(BorderBox.BORDER_ALL);
		
		// this.labels = labels;
	}
	
}