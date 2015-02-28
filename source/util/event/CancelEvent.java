// ===========================================================================
//	CancelEvent.java (part of douglas.mencken.util.event package)
// ===========================================================================

package douglas.mencken.util.event;

import java.util.EventObject;

/**
 *	<code>CancelEvent</code>
 *
 *	@version 1.01f1
 */

public class CancelEvent extends EventObject {
	
	public static final int CANCEL_FIRST = 13658;
	public static final int CANCEL_LAST = 13658;
	public static final int OPERATION_CANCELED = CANCEL_FIRST;
	
	/** This field contains event id. */
	protected int id;
	
	public CancelEvent(Object source, int id) {
		super(source);
		this.id = id;
	}
	
	public CancelEvent(Object source) {
		this(source, OPERATION_CANCELED);
	}
	
	/**
	 *	@return		the event type.
	 */
	public int getID() {
		return this.id;
	}
	
}