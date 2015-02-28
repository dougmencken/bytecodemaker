// ===========================================================================
//	Method$Exceptions.java
// ===========================================================================

package douglas.mencken.bm.plugins.attr;

import java.awt.Component;
import java.io.*;
import douglas.mencken.tools.AttributeSupport;

/**
 *	<code>Method$Exceptions</code>
 *	
 *	@version	1.0d
 *	@since		Bytecode Maker 0.6.0
 */

public class Method$Exceptions extends Object implements AttributeSupport {
	
	private int attributeLength;
	
	public Method$Exceptions() { super(); }
	
	public int getAttributeLength() {
		return this.attributeLength;
	}
	
	public String getAttributeName() {
		return "Exceptions";
	}
	
	public int getAttributeLevel() {
		return AttributeSupport.ATTRIBUTE_LEVEL_METHOD;
	}
	
	public void readAttribute(ObjectInput in) throws IOException {
		// ...
	}
	
	public void writeAttribute(ObjectOutput in) throws IOException {
		// ...
	}
	
	public Component getAttributeEditor() {
		return null;
	}
	
	public void plugin() {}
	
}