// ===========================================================================
//	EndOfStreamException.java (part of douglas.mencken.io package)
// ===========================================================================

package douglas.mencken.io;

import java.io.IOException;

/**
 *	<code>EndOfStreamException</code>
 *
 *	@version 1.01f
 */

public class EndOfStreamException extends IOException {

	public EndOfStreamException() { super(); }
	public EndOfStreamException(String message) { super(message); }
	
}