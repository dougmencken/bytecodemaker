// ===========================================================================
//	StringUtilities.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.io.*;
import java.util.StringTokenizer;

/**
 *	Utility class for some useful string operations.
 *	
 *	@version 1.22f4
 */

public final class StringUtilities extends Object {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private StringUtilities() { super(); }
	
	
	/**
	 *	This method may be useful when sorting numbers by
	 *	alphabetical order.
	 *
	 *	The result String will have <b>exactly</b> pretabTo characters.
	 *	(big strings will lose its <i>first</i> characters)
	 */
	public static String toPretabbedString(int number, int pretabTo) {
		return toPretabbedString(String.valueOf(number), pretabTo);
	}
	
	public static String toPretabbedString(String string, int pretabTo) {
		int length = string.length();
		int spaceCount = pretabTo - length;
		StringBuffer buf = new StringBuffer();
		
		if (spaceCount > 0) {
			for (int i = 0; i < spaceCount; i++) {
				buf.append(' ');
			}
			buf.append(string);
		} else {
			buf.append(string.substring(-spaceCount));
		}
		
		return buf.toString();
	}
	
	public static String makeExceptionCaughtMessage(Throwable t) {
		return makeExceptionCaughtMessage(t, true);
	}
	
	public static String makeExceptionCaughtMessage(Throwable t, boolean printStackTrace) {
		StringBuffer message = new StringBuffer(t.getClass().getName());
		message.append(" caught");
		
		String details = t.getMessage();
		if ((details != null) && (details.length() != 0)) {
			message.append(": \"").append(details).append("\"");
		}
		
		if (printStackTrace) {
			t.printStackTrace();
		}
		
		return message.toString();
	}
	
	public static String[] tokenize(String bigString) {
		return tokenize(bigString, " ,\t\n\r\f");
	}
	
	public static String[] tokenize(String bigString, String delim) {
		if (bigString == null) return null;
		if (bigString.length() == 0) return new String[0];
		
		StringTokenizer tokenizer = new StringTokenizer(bigString, delim, false);
		int tokenCount = tokenizer.countTokens();
		String[] tokens = new String[tokenCount];
		
		for (int i = 0; i < tokenCount; i++) {
			tokens[i] = tokenizer.nextToken();
		}
		
		return tokens;
	}
	
	/**
	 *	Joins two strings: s1 + ' ' + s2.
	 *	If one of strings is null or empty, it is ignored.
	 *	If both strings is null or empty, "" returned.
	 */
	public static String joinTwoStrings(String s1, String s2) {
		StringBuffer buf = new StringBuffer();
		
		boolean s1_valid = false;
		if ((s1 != null) && (s1.length() != 0)) {
			s1_valid = true;
			buf.append(s1);
		}
		if ((s2 != null) && (s2.length() != 0)) {
			if (s1_valid) buf.append(' ');
			buf.append(s2);
		}
		
		return buf.toString();
	}
	
	public static String removeFirstLastSymbols(String in) {
		if ((in == null) || (in.length() < 2)) {
			throw new IllegalArgumentException();
		}
		
		return in.substring(1, (in.length() - 1));
	}
	
	public static String getBefore(String what, char symbol) {
		if (what == null) return null;
		
		int index = what.indexOf(symbol);
		if (index < 0) return null;
		return what.substring(0, index);
	}
	
	public static String getBeforeLast(String what, char symbol) {
		if (what == null) return null;
		
		int lastIndex = what.lastIndexOf(symbol);
		if (lastIndex < 0) return null;
		return what.substring(0, lastIndex);
	}
	
	public static String getAfter(String what, char symbol) {
		if (what == null) return null;
		
		int lastIndex = what.lastIndexOf(symbol);
		if (lastIndex < 0) return null;
		return what.substring(lastIndex+1);
	}
	
	public static String getAfterFirst(String what, char symbol) {
		if (what == null) return null;
		
		int index = what.indexOf(symbol);
		if (index < 0) return null;
		return what.substring(index+1);
	}
	
	/**
	 *	The default string delimiter is ', '
	 */
	public static String stringArrayToString(String[] sArray) {
		return stringArrayToString(sArray, ", ");
	}
	
	/**
	 *	Converts the array of strings to one big single string.
	 */
	public static String stringArrayToString(String[] stringArray, String delim) {
		if (stringArray == null) return null;
		if (stringArray.length == 0) return "";
		
		StringBuffer out = new StringBuffer("");
		int count = stringArray.length;
		
		for (int i = 0; i < count; i++) {
			out.append(stringArray[i]);
			if ((i+1) < count) out.append(delim);
		}
		
		return out.toString();
	}
	
	public static String getLineNumber(StringBuffer buffer, int number) {
		if (buffer == null) {
			throw new IllegalArgumentException("null");
		}
		
		String string = buffer.toString();
		StringTokenizer tokenizer = new StringTokenizer(string, "\n", true);
		int currentLineNumber = 0;
		
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			if (!token.equals("\n")) {
				if (currentLineNumber == number) {
					return token;
				} else {
					currentLineNumber++;
				}
			} else {
				while (tokenizer.hasMoreTokens()) {
					String nextToken = tokenizer.nextToken();
					
					if (nextToken.equals("\n")) {
						if (currentLineNumber == number) {
							return "";
						} else {
							currentLineNumber++;
						}
					} else {
						if (currentLineNumber == number) {
							return nextToken;
						} else {
							currentLineNumber++;
							break;
						}
					}
				}
			}
		}
		
		throw new IndexOutOfBoundsException();
	}
	
	public static String[] extractStrings(StringBuffer inbuf) {
		return extractStrings(inbuf.toString());
	}
	
	public static String[] extractStrings(String string) {
		if (string == null) {
			throw new IllegalArgumentException("null");
		}
		if (string.length() == 0) {
			return new String[0];
		}
		
		StringTokenizer tokenizer = new StringTokenizer(string, "\n", true);
		int lineCount = countLines(string);
		String[] ret = new String[lineCount];
		
		int currentLineNumber = 0;
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			if (!token.equals("\n")) {
				ret[currentLineNumber] = token;
				currentLineNumber++;
			} else {
				while (tokenizer.hasMoreTokens()) {
					String nextToken = tokenizer.nextToken();
					
					if (nextToken.equals("\n")) {
						ret[currentLineNumber] = "";
						currentLineNumber++;
					} else {
						ret[currentLineNumber] = nextToken;
						currentLineNumber++;
						break;
					}
				}
			}
		}
		
		return ret;
	}
	
	public static String cut(String in, String cutWhat) {
		StringBuffer buf = new StringBuffer();
		int cut_len = cutWhat.length();
		int cut_index = in.indexOf(cutWhat);
		if (cut_index < 0) return in;
		
		buf.append(in.substring(0, cut_index));
		buf.append(in.substring(cut_index + cut_len));
		
		return buf.toString();
	}
	
	/**
	 *	@return		the count of symbols 'symbol' in the string 'str'.
	 */
	public static int countSymbol(String str, char symbol) {
		int count = 0;
		int len = str.length();
		
		for (int i = 0; i < len; i++) {
			if (str.charAt(i) == symbol) count++;
		}
		
		return count;
	}
	
	public static String toJavaString(final String in) throws UTFDataFormatException {
		StringBuffer buf = new StringBuffer();
		int len = in.length();
		
		for (int i = 0; i < len; i++) {
			char c = in.charAt(i);
			
			if (c == '\n') {
				buf.append("\\n");
			} else if (c == '\b') {
				buf.append("\\b");
			} else if (c == '\"') {
				buf.append("\\\"");
			} else if (c == '\\') {
				buf.append("\\\\");
			} else if ((c < 32) || (c > 127)) {
				buf.append("\\u");
				int char2, char3;
				
				switch (c >> 4) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						// 0xxx	xxxx
						buf.append(toHEX(c));
						break;
					case 12:
					case 13:
						// 110x xxxx  10xx xxxx
						if (i+1 >= len) throw new UTFDataFormatException();
						char2 = (int) in.charAt(i+1);
						if ((char2 & 0xC0) != 0x80) throw new UTFDataFormatException();
						buf.append(toHEX( ((c & 0x1F) << 6) | (char2 & 0x3F) ));
						i++;
						break;
					case 14:
						// 1110 xxxx  10xx xxxx	 10xx xxxx
						if ((i+1 >= len) || (i+2 >= len)) throw new UTFDataFormatException();
						char2 = (int) in.charAt(i+1);
						char3 = (int) in.charAt(i+2);
						
						if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80)) {
							throw new UTFDataFormatException();
						}
						buf.append(toHEX(
							((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0)
						));
						i += 2;
						break;
					default:
						// 10xx xxxx  1111 xxxx
						// throw new UTFDataFormatException("string \"" + in + '\"');
						buf.append(toHEX(c));
						break;
				}
			} else {
				buf.append(c);
			}
		}
		
		return buf.toString();
	}
	
	public static String toHEX(int value) {
		return toHEX(value, 4);
	}
	
	public static String toHEX(int value, int count) {
		StringBuffer buf = new StringBuffer();
		String hex = Integer.toHexString(value);
		
		int hlen = hex.length();
		while (count > hlen) {
			buf.append("0");
			count--;
		}
		
		buf.append(hex.toUpperCase());
		return buf.toString();
	}
	
	/**
	 *	Returns two substrings before and after 'char ch'
	 *	(<i>without</i> the character).
	 */
	public static String[] splitByChar(String s, char ch, boolean fromEnd) {
		String[] ret = { s, "" };
		int splitIndex = (fromEnd) ? s.lastIndexOf(ch) : s.indexOf(ch);
		
		if (splitIndex == -1) {
			throw new IllegalArgumentException(
				"string \"" + s + "\" doesn't contain '" +
				ch + "' character"
			);
		}
		
		ret[0] = s.substring(0, splitIndex);
		ret[1] = s.substring(splitIndex + 1);
		
		return ret;
	}
	
	public static String[] splitByChar(String s, char ch) {
		return splitByChar(s, ch, false);
	}
	
	public static int countLines(String bigString) {
		int count = 1;
		int len_m1 = bigString.length() - 1;
		
		for (int i = 1; i < len_m1; i++) {
			if (bigString.charAt(i) == '\n') {
				count++;
			}
		}
		
		return count;
	}
	
	public static int countLines(StringBuffer bigBuffer) {
		return countLines(bigBuffer.toString());
	}
	
	public static String toSeparatedByThousandsNumber(long size) {
		return toSeparatedByThousandsNumber(size, ' ');
	}
	
	public static String toSeparatedByThousandsNumber(long size, char sep) {
		StringBuffer buf = new StringBuffer(String.valueOf(size));
		int i = buf.length();
		
		while ((i -= 3) > 0) {
			buf.insert(i, sep);
		}
		
		return buf.toString();
	}
	
	public static String macToUnixLines(String src) {
		return src.replace((char)13, (char)10);
	}
	
	public static String unixToMacLines(String src) {
		return src.replace((char)10, (char)13);
	}
	
	/**
	 *	<pre>%7A -> z, %33 -> 1</pre>
	 */
	public static String normalizeString(String in) {
		StringBuffer buf = new StringBuffer();
		int len = in.length();
		
		for (int i = 0; i < len; i++) {
			if (in.charAt(i) == '%') {
				int val = Integer.parseInt(
					(String.valueOf(in.charAt(i+1)) +
					 String.valueOf(in.charAt(i+2))),
					16
				);
				buf.append((char)val);
				i += 2;
			} else {
				buf.append(in.charAt(i));
			}
		}
		
		return buf.toString();
	}
	
	public static String detab(String in, int tabLength) {
		StringBuffer buf = new StringBuffer();
		int len = in.length();
		int currPos = 0;
		
		for (int i = 0; i < len; i++) {
			int symbol = in.charAt(i);
			
			if (symbol == '\t') {
				int spacesCount = tabLength - currPos%tabLength;
				addSpaces(buf, spacesCount);
				currPos += spacesCount;
			} else if (symbol == '\n') {
				buf.append('\n');
				currPos = 0;
			} else {
				buf.append(in.charAt(i));
				currPos++;
			}
		}
		
		return buf.toString();
	}
	
	public static void addSpaces(StringBuffer buf, int count) {
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				buf.append(' ');
			}
		}
	}
	
}