// ===========================================================================
//	UnknownAttribute.java
// ===========================================================================

package douglas.mencken.bm.plugins.attr;

import java.awt.Component;
import java.io.*;

import douglas.mencken.tools.AttributeSupport;
import douglas.mencken.tools.LogMonitor;

/**
 *	<code>UnknownAttribute</code>
 *	
 *	@version	1.01a2
 *	@since		Bytecode Maker 0.6.0
 */

public class UnknownAttribute extends Object implements AttributeSupport {
	
	private int attributeLevel;
	private int attributeNameReference;
	private int attributeLength;
	
	public UnknownAttribute(int level, int attrNameRef, int length) {
		super();
		this.attributeLevel = level;
		this.attributeNameReference = attrNameRef;
		this.attributeLength = length;
	}
	
	public int getAttributeLength() {
		return this.attributeLength;
	}
	
	public String getAttributeName() {
		StringBuffer buf = new StringBuffer("* unknown attribute (@");
		buf.append(this.attributeNameReference).append(") *");
		return buf.toString();
	}
	
	public int getAttributeLevel() {
		return this.attributeLevel;
	}
	
	public void readAttribute(ObjectInput in) throws IOException {
		//if (BMPreferencesManager.getShowLog()) {
			StringBuffer buf = new StringBuffer("Unknown attribute (");
			
			switch (this.attributeLevel) {
				case AttributeSupport.ATTRIBUTE_LEVEL_CLASS:
					buf.append("\'Class\'");
					break;
				case AttributeSupport.ATTRIBUTE_LEVEL_FIELD:
					buf.append("\'Field\'");
					break;
				case AttributeSupport.ATTRIBUTE_LEVEL_METHOD:
					buf.append("\'Method\'");
					break;
				case AttributeSupport.ATTRIBUTE_LEVEL_CODE:
					buf.append("\'Code\'");
					break;
				
				default:
					buf.append("unknown");
					break;
			}
			
			buf.append(" level): @").append(Integer.toString(this.attributeNameReference));
			buf.append(", length = ").append(Integer.toString(this.attributeLength));
			
			LogMonitor.addToCurrentLog(buf.toString());
		//}
		
		in.skipBytes(this.attributeLength);
	}
	
	public void writeAttribute(ObjectOutput in) throws IOException {
		// ... ??? ...
	}
	
	public Component getAttributeEditor() {
		// use default editor
		return null;
	}
	
	public void plugin() {}
	
}
