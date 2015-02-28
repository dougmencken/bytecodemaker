// ===========================================================================
//	Code$LocalVariableTable.java
// ===========================================================================

package douglas.mencken.bm.plugins.attr;

import java.awt.Component;
import java.io.*;
import douglas.mencken.tools.AttributeSupport;

/**
 *	<code>Code$LocalVariableTable</code>
 *	
 *	@version	1.0d
 *	@since		Bytecode Maker 0.6.0
 */

public class Code$LocalVariableTable extends Object implements AttributeSupport {
	
	private int attributeLength;
	
	public Code$LocalVariableTable() { super(); }
	
	public int getAttributeLength() {
		return this.attributeLength;
	}
	
	public String getAttributeName() {
		return "LocalVariableTable";
	}
	
	public int getAttributeLevel() {
		return AttributeSupport.ATTRIBUTE_LEVEL_CODE;
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