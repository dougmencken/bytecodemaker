/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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