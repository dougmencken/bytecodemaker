// ===========================================================================
// DecHexConverter.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

/**
 *	<code>DecHexConverter</code>
 *
 *	@version 1.21f1
 */

public final class DecHexConverter extends Object {

	protected int decValue;
	protected String hexValue;
	
	/**
	 *	Default constructor.
	 */
	public DecHexConverter() {
		this(0);
	}
	
	public DecHexConverter(int dec) {
		this.setDecValue(dec);
	}
	
	public DecHexConverter(String hex) {
		this.setHexValue(hex);
	}
	
	public int getDecValue() {
		return this.decValue;
	}
	
	public String getHexValue() {
		return this.hexValue;
	}
	
	public void setDecValue(int dec) {
		this.decValue = dec;
		this.hexValue = DECtoHEX(dec);
	}
	
	public void setHexValue(String hex) {
		this.hexValue = hex;
		this.decValue = HEXtoDEC(hex);
	}
	
	public String toString() {
		StringBuffer string = new StringBuffer();
		string.append("#").append(this.hexValue);
		string.append(" = ");
		string.append(this.decValue);
		
		return string.toString();
	}
	
	
	/**
	 *	(static method)
	 */
	public static String DECtoHEX(int dec) {
		return Integer.toHexString(dec).toUpperCase();
	}
	
	/**
	 *	(static method)
	 */
	public static int HEXtoDEC(String hex) {
		return Integer.parseInt(hex, 16);
	}
	
	/**
	 *	(static method)
	 */
	public static int parseIntFromHexString(String hex) {
		if ((hex == null) || (hex.length() == 0)) {
			throw new IllegalArgumentException("'null' or empty string");
		}
		
		byte pos = 0;
		if (hex.startsWith("0x")) pos = 2;
		if (hex.startsWith("#")) pos = 1;
		
		return Integer.parseInt(hex.substring(pos), 16);
	}
	
}
