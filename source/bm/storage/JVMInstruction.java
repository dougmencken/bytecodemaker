/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.storage;

import java.io.*;

import douglas.mencken.util.ByteTransformer;

/**
 *	<code>JVMInstruction</code><br>
 *	(local to package)
 *
 *	@version 2.01d0
 */

class JVMInstruction extends Object implements Cloneable {
	
	/**
	 *	Method's program counter value for this instruction
	 *	or -1 if instruction is not bound with method.
	 */
	protected int pcValue;
	
	/**
	 *	Opcode of this instruction.
	 */
	protected byte opcode;
	
	/**
	 *	First argument of this instruction
	 *	('null' if the instruction has no first argument).
	 */
	protected String argument1;
	
	/**
	 *	Second argument of this instruction
	 *	('null' if the instruction has no second argument).
	 */
	protected String argument2;
	
	/**
	 *	Third argument of this instruction
	 *	('null' if the instruction has no third argument).
	 */
	protected String argument3;
	
	protected boolean isWide;
	
	/**
	 *	Additional arguments.
	 */
	protected String[] additionalArguments;
	
	/**
	 *	Default constructor.
	 */
	JVMInstruction() {
		this((byte)0, null, null, null);
	}
	
	JVMInstruction(byte opcode) {
		this(opcode, null, null, null);
	}
	
	JVMInstruction(byte opcode, String arg) {
		this(opcode, arg, null, null);
	}
	
	JVMInstruction(byte opcode, String arg, String arg2) {
		this(opcode, arg, arg2, null);
	}
	
	JVMInstruction(byte opcode, String arg, String arg2, String arg3) {
		this.opcode = opcode;
		this.argument1 = arg;
		this.argument2 = arg2;
		this.argument3 = arg3;
		
		this.pcValue = -1;
		this.additionalArguments = null;
		this.isWide = false;
	}
	
	int getPCValue() {
		return this.pcValue;
	}
	
	void setPCValue(int newPC) {
		this.pcValue = newPC;
	}
	
	public byte getOpcode() {
		return this.opcode;
	}
	
	public void setOpcode(byte newOpcode) {
		this.opcode = newOpcode;
	}
	
	public String getArgument1()  {
		return (this.argument1  == null) ? "" : this.argument1;
	}
	
	public String getArgument2()  {
		return (this.argument2  == null) ? "" : this.argument2;
	}
	
	public String getArgument3()  {
		return (this.argument3  == null) ? "" : this.argument3;
	}
	
	public void setArgument1(String arg1)  {
		this.argument1 = ((arg1 == null) || (arg1.length() == 0)) ? null : arg1;
	}
	
	public void setArgument2(String arg2) {
		this.argument2 = ((arg2 == null) || (arg2.length() == 0)) ? null : arg2;
	}
	
	public void setArgument3(String arg3) {
		this.argument3 = ((arg3 == null) || (arg3.length() == 0)) ? null : arg3;
	}
	
	public String[] getAdditionalArguments() {
		return this.additionalArguments;
	}
	
	public void setAdditionalArguments(String[] args) {
		this.additionalArguments = args;
	}
	
	public boolean isWide() {
		return this.isWide;
	}
	
	public void setWide(boolean wide) {
		if (this.isWide != wide) {
			this.isWide = wide;
			
			// ??? WHY ???
			//if (wide) {
			//	this.pcValue--;
			//}
		}
	}
	
	public Object clone() {
		// ...
		return this;
	}
	
	// public boolean equals(Object obj) {
		// ...
	// }
	
}
