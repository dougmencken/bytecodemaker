// ===========================================================================
// ErrOutFrame.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.Frame;
import douglas.mencken.beans.TextView;
import douglas.mencken.tools.TextViewPopupMenu;
import douglas.mencken.exceptions.TooManyInstancesException;

/**
 *	<code>ErrOutFrame</code>
 *
 *	@version 1.04f3
 */

class ErrOutFrame extends Frame {
	
	private static int instances_count = 0;
	
	ErrOutFrame(String title, TextView textView) throws TooManyInstancesException {
		super(title);
		instances_count++;
		if (instances_count > 2) {
			throw new TooManyInstancesException(super.getClass().getName());
		}
		
		textView.add(new TextViewPopupMenu(textView, "java." + title));
		super.add(textView);
		super.setLocation(3, 100 + 100*instances_count);
		super.setSize(600, 250);
	}
	
}
