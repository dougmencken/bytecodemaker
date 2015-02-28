// ===========================================================================
//	AttributeSupport.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.Component;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

/**
 *	<code>AttributeSupport</code><br>
 *	Interface for attribute support plug-ins.
 *
 *	@version 1.02f1
 */

public interface AttributeSupport {
	
	public static final int ATTRIBUTE_LEVEL_CLASS = 1;
	public static final int ATTRIBUTE_LEVEL_FIELD = 4;
	public static final int ATTRIBUTE_LEVEL_METHOD = 5;
	public static final int ATTRIBUTE_LEVEL_CODE = 8;
	
	/**
	 *	@return		the length of this attribute.
	 */
	public int getAttributeLength();
	
	/**
	 *	@return		the name of supported attribute.
	 */
	public String getAttributeName();
	
	/**
	 *	@return		the level of supported attribute.
	 */
	public int getAttributeLevel();
	
	/**
	 *	Reads attribute from the ObjectInput.
	 */
	public void readAttribute(ObjectInput in) throws IOException;
	
	/**
	 *	Writes attribute to the ObjectOutput.
	 */
	public void writeAttribute(ObjectOutput in) throws IOException;
	
	/**
	 *	@return		editor of supported attribute.
	 *				Use 'null' if there is no editor.
	 */
	public Component getAttributeEditor();
	
	/**
	 *	A standard method for any plug-in.
	 */
	public void plugin();
	
}
