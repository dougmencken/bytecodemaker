/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

/**
 *	<code>ByteTransformer</code>
 *	
 *	@version 1.30f1
 */

public class ByteTransformer extends Object {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private ByteTransformer() {}
	
	
	public static byte[] removeFirstBytes(byte[] src, int count) {
		byte[] out = new byte[src.length - count];
		
		for (int i = count; i < src.length; i++) {
			out[i-count] = src[i];
		}
		
		return out;
	}
	
	public static byte[] removeLastBytes(byte[] src, int count) {
		byte[] out = new byte[src.length - count];
		
		for (int i = 0; i < (src.length - count); i++) {
			out[i] = src[i];
		}
		
		return out;
	}
	
	public static int toUnsignedByte(byte[] src, int i) {
		return (src[i] < 0) ? (256 + src[i]) : src[i];
	}
	
	public static int toUnsignedByte(byte b) {
		int int_b = (int)b;
		if (b < 0) int_b = 256 + b;
		
		return int_b;
	}
	
	public static short toSignedShort(byte[] src, int i) {
		return (short)toUnsignedShort(src, i, i+1);
	}
	
	public static short toSignedShort(byte[] src, int i1, int i2) {
		return (short)toUnsignedShort(src, i1, i2);
	}
	
	public static int toUnsignedShort(byte[] src, int i) {
		return toUnsignedShort(src, i, i+1);
	}
	
	public static int toUnsignedShort(byte[] src, int i1, int i2) {
		int index1 = (src[i1] < 0) ? (256 + src[i1]) : src[i1];
		int index2 = (src[i2] < 0) ? (256 + src[i2]) : src[i2];
		
		return (index1 << 8) + (index2 << 0);
	}
	
	public static int toInteger(byte[] src, int i) {
		return toInteger(src, i, i+1, i+2, i+3);
	}
	
	public static int toInteger(byte[] src, int i1, int i2, int i3, int i4) {
		int index1 = (src[i1] < 0) ? (256 + src[i1]) : src[i1];
		int index2 = (src[i2] < 0) ? (256 + src[i2]) : src[i2];
		int index3 = (src[i3] < 0) ? (256 + src[i3]) : src[i3];
		int index4 = (src[i4] < 0) ? (256 + src[i4]) : src[i4];
		
		return ((index1 << 24) + (index2 << 16) + (index3 << 8) + (index4 << 0));
	}
	
	public static long toLong(byte[] src, int i) {
		return toLong(src, i, i+1, i+2, i+3, i+4, i+5, i+6, i+7);
	}
	
	public static long toLong(	byte[] src,
								int i1, int i2, int i3, int i4,
								int i5, int i6, int i7, int i8) {
		return ((long)(toInteger(src, i1, i2, i3, i4)) << 32) +
				(toInteger(src, i5, i6, i7, i8) & 0xFFFFFFFFL);
	}
	
	public static float toFloat(byte[] src, int i) {
		return Float.intBitsToFloat(toInteger(src, i));
	}
	
	public static double toDouble(byte[] src, int i) {
		return Double.longBitsToDouble(toLong(src, i));
	}
	
	public static byte[] extractBytesFromShort(int b) {
		byte[] ret = new byte[2];
		ret[0] = (byte)((b >>> 8) & 0xFF);
		ret[1] = (byte)((b >>> 0) & 0xFF);
		
		return ret;
	}
	
	public static byte[] extractBytesFromInt(int b) {
		byte[] ret = new byte[4];
		ret[0] = (byte)((b >>> 24) & 0xFF);
		ret[1] = (byte)((b >>> 16) & 0xFF);
		ret[2] = (byte)((b >>>  8) & 0xFF);
		ret[3] = (byte)((b >>>	 0) & 0xFF);
		
		return ret;
	}
	
	public static byte[] extractBytesFromLong(long v) {
		byte[] ret = new byte[8];
		ret[0] = (byte)((int)(v >>> 56) & 0xFF);
		ret[1] = (byte)((int)(v >>> 48) & 0xFF);
		ret[2] = (byte)((int)(v >>> 40) & 0xFF);
		ret[3] = (byte)((int)(v >>> 32) & 0xFF);
		ret[4] = (byte)((int)(v >>> 24) & 0xFF);
		ret[5] = (byte)((int)(v >>> 16) & 0xFF);
		ret[6] = (byte)((int)(v >>>  8) & 0xFF);
		ret[7] = (byte)((int)(v >>>  0) & 0xFF);
		
		return ret;
	}
	
	public static byte[] extractBytesFromFloat(float v) {
		return extractBytesFromInt(Float.floatToIntBits(v));
	}
	
	public static byte[] extractBytesFromDouble(double v) {
		return extractBytesFromLong(Double.doubleToLongBits(v));
	}
	
}
