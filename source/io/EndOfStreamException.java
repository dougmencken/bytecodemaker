/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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