// ===========================================================================
//	MiniStack.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.io.*;
import java.util.EmptyStackException;

/**
 *	<code>MiniStack</code>
 *
 *	@version 1.60f1
 */

public class MiniStack extends Object
implements Cloneable, Externalizable {
	
	protected int stackSize;
	protected Object[] stack;
	protected int topPos = -1;
	
	public MiniStack(int maxstack) {
		this.stackSize = maxstack;
		this.stack = new Object[maxstack];
	}
	
	public void push(Object obj) {
		if (topPos >= (stackSize - 1)) {
			throw new StackOverflowError(this.infoString());
		}
		
		this.topPos++;
		stack[topPos] = obj;
	}
	
	public boolean canPush() {
		return (topPos < (stackSize - 1));
	}
	
	public void dup() {
		if (topPos >= (stackSize - 1)) {
			throw new StackOverflowError(this.infoString());
		}
		
		this.topPos++;																
		stack[topPos] = stack[topPos-1];
	}
	
	public Object pop() {
		if (topPos == -1) {
			throw new EmptyStackException();
		}
		
		Object top = stack[topPos];
		stack[topPos] = null;
		this.topPos--;
		
		return top;
	}
	
	public void swap() {
		if (topPos < 1) {
			throw new EmptyStackException();
		}
		
		Object temp = stack[topPos];
		stack[topPos] = stack[topPos-1];
		stack[topPos-1] = temp;
	}
	
	public Object peek() {
		if (topPos == -1) {
			throw new EmptyStackException();
		}
		return this.stack[this.topPos];
	}
	
	// ret[0] -> stack top
	public Object[] getStackValues() {
		int size = topPos + 1;
		Object[] ret = new Object[size];
		
		for (int i = 0; i < size; i++) {
			ret[i] = this.stack[size - i-1];
		}
		
		return ret;
	}
	
	public void clear() {
		this.stack = new Object[this.stackSize];
		this.topPos = -1;
	}
	
	public boolean isEmpty() {
		return (this.topPos == -1);
	}
	
	public int getCurrentSize() {
		return (this.topPos + 1);
	}
	
	public Object clone() {
		MiniStack ret = new MiniStack(this.stackSize);
		ret.topPos = this.topPos;
		System.arraycopy(this.stack, 0, ret.stack, 0, this.stackSize);
		return ret;
	}
	
	public String toString() {
		StringBuffer ret = new StringBuffer("MiniStack {\n");
		for (int i = this.topPos; i >= 0; i--) {
			ret.append('\t');
			ret.append((stack[i] == null) ? "null" : stack[i].toString());
			ret.append('\n');
		}
		ret.append('}');
		
		return ret.toString();
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int stackSize = in.readInt();
		this.stack = new Object[stackSize];
		
		for (int i = 0; i < stackSize; i++) {
			this.stack[i] = in.readObject();
		}
		
		this.topPos = in.readInt();
	}
	
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(this.stackSize);
		
		for (int i = 0; i < this.stackSize; i++) {
			out.writeObject(this.stack[i]);
		}
		
		out.writeInt(this.topPos);
	}
	
	private String infoString() {
		StringBuffer buf = new StringBuffer();
		buf.append("stackSize = ").append(this.stackSize);
		buf.append(", topPos = ").append(this.topPos);
		
		return buf.toString();
	}
	
}