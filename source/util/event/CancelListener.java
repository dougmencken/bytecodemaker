/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util.event;

import java.util.EventListener;

/**
 *	<code>CancelListener</code>
 *
 *	@version 1.0
 */

public interface CancelListener extends EventListener {
	
    public void operationCanceled(CancelEvent e);
    
}