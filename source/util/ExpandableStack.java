/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

import java.util.EmptyStackException;

/**
 *	<code>ExpandableStack</code>
 *
 *	@version 1.03f
 */

public class ExpandableStack extends MiniStack {
	
	public ExpandableStack(int initialSize) {
		super(initialSize);
	}
	
	protected void expandStack() {
		int size = super.stackSize;
		Object[] expandedStack = new Object[size+1];
		System.arraycopy(super.stack, 0, expandedStack, 0, size);
		
		super.stack = expandedStack;
		super.stackSize++;
	}
	
	public void push(Object obj) {
		if (super.topPos >= (super.stackSize - 1)) {
			expandStack();
		}
		
		super.topPos++;
		stack[topPos] = obj;
	}
	
	public boolean canPush() {
		return true;
	}
	
	public void dup() {
		if (topPos >= (super.stackSize - 1)) {
			expandStack();
		}
		
		topPos++;
		if (topPos > 0) {															
			stack[topPos] = stack[topPos-1];
		} else {
			throw new EmptyStackException();
		}
	}
	
	public int getCurrentCapacity() {
		return super.stackSize;
	}
	
	public Object clone() {
		ExpandableStack ret = new ExpandableStack(super.stackSize);
		ret.topPos = super.topPos;
		System.arraycopy(super.stack, 0, ret.stack, 0, super.stackSize);
		return ret;
	}
	
	public String toString() {
		StringBuffer ret = new StringBuffer("ExpandableStack {\n");
		for (int i = super.topPos; i >= 0; i--) {
			ret.append('\t');
			ret.append((stack[i] == null) ? "null" : stack[i].toString());
			ret.append('\n');
		}
		ret.append('}');
		
		return ret.toString();
	}
	
}