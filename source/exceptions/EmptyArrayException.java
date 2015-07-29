/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.exceptions;

/**
 *	<code>EmptyArrayException</code>
 *
 *	@version 1.0
 */

public class EmptyArrayException extends RuntimeException {
	
	public EmptyArrayException() { super(); }
	public EmptyArrayException(String message) { super(message); }
	
}
