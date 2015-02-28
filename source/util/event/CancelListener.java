// ===========================================================================
//	CancelListener.java (part of douglas.mencken.util.event package)
// ===========================================================================

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