/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.plugins.attr;

import java.awt.Component;
import java.io.*;
import douglas.mencken.tools.AttributeSupport;

/**
 *	<code>Method$Deprecated</code>
 *	
 *	@version	1.0d
 *	@since		Bytecode Maker 0.6.0
 */

public class Method$Deprecated extends Object implements AttributeSupport {
	
	private int attributeLength;
	
	public Method$Deprecated() { super(); }
	
	public int getAttributeLength() {
		return this.attributeLength;
	}
	
	public String getAttributeName() {
		return "Deprecated";
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
