// ===========================================================================
//	SpecialStack.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.io.*;
import java.util.EmptyStackException;

/**
 *	<code>SpecialStack</code>
 *
 *	@version 1.03a1
 */

public class SpecialStack extends ExpandableStack {
	
	private Object[][] specialData;
	
	public SpecialStack(int initialSize) {
		super(initialSize);
		this.specialData = new Object[initialSize][];
	}
	
	private void expandSpecialData() {
		int size = this.specialData.length;
		Object[][] expandedData = new Object[size+1][];
		System.arraycopy(this.specialData, 0, expandedData, 0, size);
		
		this.specialData = expandedData;
	}
	
	public void push(Object obj, Object[] specialData) {
		int stackSize = super.stackSize;
		super.push(obj);
		int newStackSize = super.stackSize;
		
		if (stackSize != newStackSize) {
			this.expandSpecialData();
		}
		
		this.specialData[super.topPos] = specialData;
	}
	
	public void push(Object obj) {
		this.push(obj, new Object[0]);
	}
	
	public Object pop() {
		if (super.topPos == -1) {
			throw new EmptyStackException();
		}
		
		this.specialData[super.topPos] = null;
		return super.pop();
	}
	
	public Object[] peekSpecialData() {
		if (super.topPos == -1) {
			throw new EmptyStackException();
		}
		return this.specialData[super.topPos];
	}
	
	public void dup() {
		int stackSize = super.stackSize;
		super.dup();
		int newStackSize = super.stackSize;
		
		if (stackSize != newStackSize) {
			this.expandSpecialData();
		}
		
		if (super.topPos > 0) {
			this.specialData[super.topPos] = this.specialData[super.topPos - 1];
		} else {
			throw new EmptyStackException();
		}
	}
	
	public void swap() {
		if (topPos < 1) {
			throw new EmptyStackException();
		}
		
		super.swap();
		
		Object[] temp = this.specialData[topPos];
		this.specialData[topPos] = this.specialData[topPos-1];
		this.specialData[topPos-1] = temp;
	}
	
	public void clear() {
		super.stack = new Object[super.stackSize];
		this.specialData = new Object[super.stackSize][];
		super.topPos = -1;
	}
	
	public Object clone() {
		SpecialStack ret = new SpecialStack(super.stackSize);
		ret.topPos = super.topPos;
		System.arraycopy(super.stack, 0, ret.stack, 0, super.stackSize);
		
		ret.specialData = new Object[super.stackSize][];
		System.arraycopy(this.specialData, 0, ret.specialData, 0, super.stackSize);
		
		return ret;
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		this.specialData = (Object[][])in.readObject();
	}
	
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeObject(this.specialData);
	}
	
	public String toString() {
		StringBuffer ret = new StringBuffer("SpecialStack {\n");
		for (int i = super.topPos; i >= 0; i--) {
			ret.append('\t');
			ret.append((stack[i] == null) ? "null" : stack[i].toString());
			
			if (this.specialData[i] != null) {
				ret.append(" { ");
				
				int datalen = this.specialData[i].length;
				for (int j = 0; j < datalen; j++) {
					ret.append(this.specialData[i][j].toString());
					if ((j+1) != datalen) {
						ret.append(", ");
					}
				}
				
				ret.append(" }");
			}
			
			ret.append('\n');
		}
		ret.append('}');
		
		return ret.toString();
	}
	
}