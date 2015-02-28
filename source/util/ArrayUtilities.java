// ===========================================================================
//	ArrayUtilities.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.util.Vector;

/**
 *	<code>ArrayUtilities</code> is an utility class that contains
 *	some useful array operations such as 'addElement' and 'removeElement'.
 *	(All methods are static.)
 *
 *	@version 1.53f
 */

public final class ArrayUtilities extends Object {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private ArrayUtilities() { super(); }
	
	
	public static Object[] addElement(final Object[] array, Object what) {
		int len = 0;
		if (array != null) {
			len = array.length;
		}
		
		Object[] ret = new Object[len+1];
		if (array != null) {
			System.arraycopy(array, 0, ret, 0, len);
		}
		ret[len] = what;
		
		return ret;
	}
	
	public static Object[] addElement(final Object[] array, Object what, int pos) {
		if (array == null) {
			if (pos != 0) {
				throw new ArrayIndexOutOfBoundsException();
			}
			
			Object[] ret = { what };
			return ret;
		}
		
		int len = array.length;
		Object[] ret = new Object[len+1];
		
		System.arraycopy(array, 0, ret, 0, pos);
		ret[pos] = what;
		System.arraycopy(array, pos, ret, pos+1, len-pos);
		
		return ret;
	}
	
	public static int[] addElement(final int[] array, int what) {
		int len = 0;
		if (array != null) {
			len = array.length;
		}
		
		int[] ret = new int[len+1];
		if (array != null) {
			System.arraycopy(array, 0, ret, 0, len);
		}
		ret[len] = what;
		
		return ret;
	}
	
	public static int[] addElement(final int[] array, int what, int pos) {
		if (array == null) {
			if (pos != 0) {
				throw new ArrayIndexOutOfBoundsException();
			}
			
			int[] ret = { what };
			return ret;
		}
		
		int len = array.length;
		int[] ret = new int[len+1];
		
		System.arraycopy(array, 0, ret, 0, pos);
		ret[pos] = what;
		System.arraycopy(array, pos, ret, pos+1, len-pos);
		
		return ret;
	}
	
	public static Object[] removeElement(final Object[] array, int pos) {
		if (array == null) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int len = array.length;
		Object[] ret = new Object[len-1];
		
		System.arraycopy(array, 0, ret, 0, pos);
		System.arraycopy(array, pos+1, ret, pos, len-pos-1);
		
		return ret;
	}
	
	public static int[] removeElement(final int[] array, int pos) {
		if (array == null) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int len = array.length;
		int[] ret = new int[len-1];
		
		System.arraycopy(array, 0, ret, 0, pos);
		System.arraycopy(array, pos+1, ret, pos, len-pos-1);
		
		return ret;
	}
	
	public static int countUniqueElements(final Vector in) {
		int len = in.size();
		int uniqueCount = 0;
		
		for (int i = 0; i < len; i++) {
			boolean isUnique = true;
			for (int j = i+1; j < len; j++) {
				if (in.elementAt(i).equals(in.elementAt(j))) {
					isUnique = false;
					break;
				}
			}
			
			if (isUnique) uniqueCount++;
		}
		
		return uniqueCount;
	}
	
	public static int countUniqueElements(final Object[] in) {
		int len = in.length;
		int uniqueCount = 0;
		
		for (int i = 0; i < len; i++) {
			boolean isUnique = true;
			for (int j = i+1; j < len; j++) {
				if (in[i].equals(in[j])) {
					isUnique = false;
					break;
				}
			}
			
			if (isUnique) uniqueCount++;
		}
		
		return uniqueCount;
	}
	
	public static int countUniqueElements(final int[] in) {
		int len = in.length;
		int uniqueCount = 0;
		
		for (int i = 0; i < len; i++) {
			boolean isUnique = true;
			for (int j = i+1; j < len; j++) {
				if (in[i] == in[j]) {
					isUnique = false;
					break;
				}
			}
			
			if (isUnique) uniqueCount++;
		}
		
		return uniqueCount;
	}
	
	public static Object[] unique(final Object[] in) {
		int len = in.length;
		int uniqueCount = countUniqueElements(in);
		Object[] out = new Object[uniqueCount];
		int pos = 0;
		
		for (int i = 0; i < len; i++) {
			boolean isUnique = true;
			for (int j = i+1; j < len; j++) {
				if (in[i].equals(in[j])) {
					isUnique = false;
					break;
				}
			}
			
			if (isUnique) {
				out[pos] = in[i];
				pos++;
			}
		}
		
		return out;
	}
	
	public static int[] uniqueIntArray(final int[] in) {
		int len = in.length;
		int uniqueCount = countUniqueElements(in);
		int[] out = new int[uniqueCount];
		int pos = 0;
		
		for (int i = 0; i < len; i++) {
			boolean isUnique = true;
			for (int j = i+1; j < len; j++) {
				if (in[i] == in[j]) {
					isUnique = false;
					break;
				}
			}
			
			if (isUnique) {
				out[pos] = in[i];
				pos++;
			}
		}
		
		return out;
	}
	
	public static String[] vectorToStringArray(final Vector in) {
		if ((in != null) && (!in.isEmpty())) {
			int size = in.size();
			String[] out = new String[size];
			
			for (int i = 0; i < size; i++) {
				out[i] = (String)in.elementAt(i);
			}
			
			return out;
		} else {
			return null;
		}
	}
	
	public static int[] vectorToIntArray(final Vector in) {
		if ((in != null) && (!in.isEmpty())) {
			int size = in.size();
			int[] out = new int[size];
			
			for (int i = 0; i < size; i++) {
				out[i] = ((Integer)in.elementAt(i)).intValue();
			}
			
			return out;
		} else {
			return null;
		}
	}
	
	public static Integer[] cover(final int[] in) {
		int len = in.length;
		Integer[] out = new Integer[len];
		
		for (int i = 0; i < len; i++) {
			out[i] = new Integer(in[i]);
		}
		
		return out;
	}
	
	public static Float[] cover(final float[] in) {
		int len = in.length;
		Float[] out = new Float[len];
		
		for (int i = 0; i < len; i++) {
			out[i] = new Float(in[i]);
		}
		
		return out;
	}
	
	public static Long[] cover(final long[] in) {
		int len = in.length;
		Long[] out = new Long[len];
		
		for (int i = 0; i < len; i++) {
			out[i] = new Long(in[i]);
		}
		
		return out;
	}
	
	public static Double[] cover(final double[] in) {
		int len = in.length;
		Double[] out = new Double[len];
		
		for (int i = 0; i < len; i++) {
			out[i] = new Double(in[i]);
		}
		
		return out;
	}
	
	public static int[] uncoverToInt(final Object[] in) {
		int len = in.length;
		int[] out = new int[len];
		
		for (int i = 0; i < len; i++) {
			out[i] = ((Integer)(in[i])).intValue();
		}
		
		return out;
	}
	
	public static int[] stringArrayToIntArray(final String[] in) {
		int len = in.length;
		int[] out = new int[len];
		
		for (int i = 0; i < len; i++) {
			out[i] = Integer.parseInt(in[i]);
		}
		
		return out;
	}
	
	public static boolean isMaxElement(final int[] array, int el) {
		if ((array == null) || (array.length == 0)) {
			throw new IllegalArgumentException();
		}
		
		int len = array.length;
		if (len == 1) return true;
		
		int curr_max = array[0];
		for (int i = 0; i < len; i++) {
			if (array[i] > curr_max) curr_max = array[i];
		}
		
		if (el == curr_max) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isMinElement(final int[] array, int el) {
		if ((array == null) || (array.length == 0)) {
			throw new IllegalArgumentException();
		}
		
		int len = array.length;
		if (len == 1) return true;
		
		int curr_min = array[0];
		for (int i = 0; i < len; i++) {
			if (array[i] < curr_min) curr_min = array[i];
		}
		
		if (el == curr_min) {
			return true;
		} else {
			return false;
		}
	}
	
	public static int getMaxElement(final int[] array) {
		if ((array == null) || (array.length == 0)) {
			throw new IllegalArgumentException();
		}
		
		int len = array.length;
		if (len == 1) return array[0];
		
		int curr_max = array[0];
		for (int i = 0; i < len; i++) {
			if (array[i] > curr_max) curr_max = array[i];
		}
		
		return curr_max;
	}
	
	public static int getMinElement(final int[] array) {
		if ((array == null) || (array.length == 0)) {
			throw new IllegalArgumentException();
		}
		
		int len = array.length;
		if (len == 1) return array[0];
		
		int curr_min = array[0];
		for (int i = 0; i < len; i++) {
			if (array[i] < curr_min) curr_min = array[i];
		}
		
		return curr_min;
	}
	
	/**
	 *	@return		the index of matching element, -1 if no match found
	 */
	public static int findMatchingElement(final int[] array, int el) {
		if ((array == null) || (array.length == 0)) {
			throw new IllegalArgumentException();
		}
		
		int len = array.length;
		
		for (int i = 0; i < len; i++) {
			if (array[i] == el) return i;
		}
		
		return -1;
	}
	
	/**
	 *	@return		the index of matching element, -1 if no match found
	 */
	public static int findMatchingElement(final String[] array, String el) {
		if ((array == null) || (array.length == 0)) {
			throw new IllegalArgumentException();
		}
		
		int len = array.length;
		
		for (int i = 0; i < len; i++) {
			if (array[i].equals(el)) return i;
		}
		
		return -1;
	}
	
	/**
	 *	@return		the index of matching element, -1 if no match found
	 */
	public static int findMatchingElement(final Vector vector, Object obj) {
		if (vector == null) {
			throw new IllegalArgumentException();
		}
		
		int size = vector.size();
		
		for (int i = 0; i < size; i++) {
			if (vector.elementAt(i).equals(obj)) return i;
		}
		
		return -1;
	}
	
	public static Object[] move(final Object[] array, int pos, int new_pos) {
		int len = array.length;
		
		if (pos == new_pos) return array;
		if ((pos >= len) || (new_pos >= len) || (pos < 0) || (new_pos < 0)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Object[] ret = new Object[len];
		
		if (pos > new_pos) {
			System.arraycopy(array, 0, ret, 0, new_pos);
			ret[new_pos] = array[pos];
			System.arraycopy(array, pos, ret, pos+1, pos-new_pos);
			System.arraycopy(array, pos+1, ret, pos+1, len-pos-1);
		} else {
			System.arraycopy(array, 0, ret, 0, pos);
			System.arraycopy(array, pos+1, ret, pos, new_pos-pos);
			ret[new_pos] = array[pos];
			System.arraycopy(array, new_pos+1, ret, new_pos+1, len-new_pos-1);
		}
		
		return ret;
	}
	
	public static String[] move(final String[] array, int pos, int new_pos) {
		int len = array.length;
		
		if (pos == new_pos) return array;
		if ((pos >= len) || (new_pos >= len) || (pos < 0) || (new_pos < 0)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		String[] ret = new String[len];
		
		if (pos > new_pos) {
			System.arraycopy(array, 0, ret, 0, new_pos);
			ret[new_pos] = array[pos];
			System.arraycopy(array, pos, ret, pos+1, pos-new_pos);
			System.arraycopy(array, pos+1, ret, pos+1, len-pos-1);
		} else {
			System.arraycopy(array, 0, ret, 0, pos);
			System.arraycopy(array, pos+1, ret, pos, new_pos-pos);
			ret[new_pos] = array[pos];
			System.arraycopy(array, new_pos+1, ret, new_pos+1, len-new_pos-1);
		}
		
		return ret;
	}
	
	public static int[] move(final int[] array, int pos, int new_pos) {
		int len = array.length;
		
		if (pos == new_pos) return array;
		if ((pos >= len) || (new_pos >= len) || (pos < 0) || (new_pos < 0)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int[] ret = new int[len];
		
		if (pos > new_pos) {
			System.arraycopy(array, 0, ret, 0, new_pos);
			ret[new_pos] = array[pos];
			System.arraycopy(array, pos, ret, pos+1, pos-new_pos);
			System.arraycopy(array, pos+1, ret, pos+1, len-pos-1);
		} else {
			System.arraycopy(array, 0, ret, 0, pos);
			System.arraycopy(array, pos+1, ret, pos, new_pos-pos);
			ret[new_pos] = array[pos];
			System.arraycopy(array, new_pos+1, ret, new_pos+1, len-new_pos-1);
		}
		
		return ret;
	}
	
	public static void swap(final Object[] array, int pos) {
		swap(array, pos, pos + 1);
	}
	
	public static void swap(final Object[] array, int pos, boolean forward) {
		int pos2 = (forward) ? pos + 1 : pos - 1;
		swap(array, pos, pos2);
	}
	
	public static void swap(final Object[] array, int pos, int pos2) {
		if (pos == pos2) return;
		
		Object temp = array[pos];
		array[pos] = array[pos2];
		array[pos2] = temp;
	}
	
	public static void swap(final int[] array, int pos, int pos2) {
		if (pos == pos2) return;
		
		int temp = array[pos];
		array[pos] = array[pos2];
		array[pos2] = temp;
	}
	
	public static void sort(int[] array) {
		quickSort(array, 0, array.length - 1);
	}
	
	public static void sortWith(int[] array, int[] array2) {
		quickSortWith(array, 0, array.length - 1, array2);
	}
	
	private static void quickSort(int[] array, int lo0, int hi0) {
		int lo = lo0;
		int hi = hi0;
		int mid;
		
		if (hi0 > lo0) {
			mid = array[(lo0 + hi0) / 2];
			
			while (lo <= hi) {
				while ((lo < hi0) && (array[lo] < mid)) ++lo;
				while ((hi > lo0) && (array[hi] > mid)) --hi;
				
				if (lo <= hi) {
					swap(array, lo, hi);
					++lo;
					--hi;
				}
			}
			
			if (lo0 < hi) quickSort(array, lo0, hi);
			if (lo < hi0) quickSort(array, lo, hi0);
		}
	}
	
	private static void quickSortWith(int[] array, int lo0, int hi0, int[] array2) {
		int lo = lo0;
		int hi = hi0;
		int mid;
		
		if (hi0 > lo0) {
			mid = array[(lo0 + hi0) / 2];
			
			while (lo <= hi) {
				while ((lo < hi0) && (array[lo] < mid)) ++lo;
				while ((hi > lo0) && (array[hi] > mid)) --hi;
				
				if (lo <= hi) {
					if (array[lo] != array[hi]) {
						swap(array, lo, hi);
						swap(array2, lo, hi);
					}
					
					++lo;
					--hi;
				}
			}
			
			if (lo0 < hi) quickSortWith(array, lo0, hi, array2);
			if (lo < hi0) quickSortWith(array, lo, hi0, array2);
		}
	}
	
}