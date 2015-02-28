// ===========================================================================
//	Method$Synthetic.java
// ===========================================================================

package douglas.mencken.bm.plugins.attr;

import java.awt.Component;
import java.io.*;
import douglas.mencken.tools.AttributeSupport;

/**
 *	<code>Method$Synthetic</code>
 *	
 *	@version	1.0d
 *	@since		Bytecode Maker 0.6.0
 */

public class Method$Synthetic extends Object implements AttributeSupport {
	
	private int attributeLength;
	
	public Method$Synthetic() { super(); }
	
	public int getAttributeLength() {
		return this.attributeLength;
	}
	
	public String getAttributeName() {
		return "Synthetic";
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
