// ===========================================================================
// TooManyInstancesException.java (part of douglas.mencken.exceptions package)
// ===========================================================================

package douglas.mencken.exceptions;

/**
 *	<code>TooManyInstancesException</code>
 *
 *	@version 1.0
 */

public class TooManyInstancesException extends InstantiationException {

	public TooManyInstancesException() { super(); }
	public TooManyInstancesException(String message) { super(message); }
	
}
