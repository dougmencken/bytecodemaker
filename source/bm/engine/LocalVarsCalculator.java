/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.engine;

import java.io.*;
import java.util.EmptyStackException;

import douglas.mencken.util.ByteTransformer;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.util.SpecialStack;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.tools.LogMonitor;
import douglas.mencken.bm.storage.*;

/**
 *	<code>LocalVarsCalculator</code>
 *
 *	@version 0.78d4
 */

public class LocalVarsCalculator extends Object implements Externalizable {
	
	protected JavaMethod method;
	protected LocalVariableValues[] localVarValues;
	
	public LocalVarsCalculator(JavaMethod method) {
		this.method = method;
		
		//try {
			this.localVarValues = LocalVarsCalculator.fromMethod(method);
		//} catch (BadBytecodesException bbe) {
		/*	UsefulMessageDialogs.doErrorDialog(
				bbe.getClass().getName() + " (caught in " + super.getClass().getName() +
				" for method '" + method.getMethodName() + "'): " + bbe.getMessage()
			);
			this.localVarValues = null;*/
		//}
	}
	
	/**
	 *	Writes local variable values to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(method);
		
		int length = this.localVarValues.length;
		out.writeInt(length);
		
		for (int i = 0; i < length; i++) {
			out.writeObject(this.localVarValues[i].getLocalVariables());
		}
	}
	
	/**
	 *	Reads local variable values from the ObjectInput.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.method = (JavaMethod)in.readObject();
		
		int length = in.readInt();
		this.localVarValues = new LocalVariableValues[length];
		
		for (int i = 0; i < length; i++) {
			this.localVarValues[i].setLocalVariables((LocalVariableWithValue[])in.readObject());
		}
	}
	
	/**
	 *	(static method)
	 */
	public static LocalVariableValues[] fromMethod(JavaMethod method)
	throws EmptyStackException, StackOverflowError {
/////		try {
			int codeLength = method.getCodeLength();
///////			int maxlocals = method.getMaxLocals();
///////			if ((codeLength == 0) || (maxlocals == 0)) {
				return null;
///////			}
			
/////			LocalVariableValues[] localVarValues = new LocalVariableValues[codeLength];
//////////.			JavaBytecode[] bytecodes = BytecodeLocksmith.extractBytecodes(method);
			
			// initial values
/////			localVarValues[0] = new LocalVariableValues(method);
//////////.			calculateCodeBlock(method, bytecodes, 0, codeLength, localVarValues);//, false);
			
/////			return localVarValues;
/////		} catch (EmptyStackException exc) {
/////			throw exc;
/////			/*throw new BadBytecodesException(
/////				"LocalVarsCalculator.fromMethod(JavaMethod) caught EmptyStackException"
/////			);*/
/////		} catch (StackOverflowError err) {
/////			throw err;
/////			/*throw new BadBytecodesException(
/////				"LocalVarsCalculator.fromMethod(JavaMethod) caught StackOverflowError"
/////			);*/
/////		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	protected static void calculateCodeBlock(JavaMethod method, final JavaBytecode[] bytecodes,
						int startPC, int endPC,
						LocalVariableValues[] localVarValues) //, boolean _add)
	{ //////////////////// throws BadBytecodesException {
		LocalVariableValues currentVarValues =
					(LocalVariableValues)(localVarValues[startPC].clone());
		StackCalculator s_c = method.getStackCalculator();
		
		for (int pc = startPC; pc < endPC; pc++) {
			JavaBytecode currBytecode = bytecodes[pc];
			if (currBytecode != null) {
				byte currOpcode = currBytecode.getOpcode();
				String arg = currBytecode.getArg();
				String arg2 = currBytecode.getArg2();
				
				switch (currOpcode) {
					// istore, lstore, fstore, dstore, astore
					case 54:
					case 55:
					case 56:
					case 57:
					case 58:
					{
						int previous_pc = BytecodeBlock.calculatePreviousPC(bytecodes, pc);
						SpecialStack stack = null;
						try {
							stack = s_c.getStackAt(previous_pc);
						} catch (NullPointerException npe_ex) {
							throw new InternalError(
								"method '" + method.getMethodName() +
								"', pc = " + previous_pc + ": no stack (null)"
							);
						}
						
						int slot = Integer.parseInt(arg);
						
						try {
							currentVarValues.setVariableValueAt(slot, stack.pop());
						} catch (IndexOutOfBoundsException iob_ex) {
							// try to find the ret's 'jvm_ret_addr'
							String slot_type = findLocalVariableType(slot, method);
							boolean outOfBounds = true;
							
							if (slot_type.equals("jvm_ret_addr")) {
								// add local variable: 'jvm_ret_addr'
								if (slot > method.getLocalVariables().length) {
									if (method.addLocalVariable(LocalVariable.makeLocalVariable(
										method, slot, "v" + slot, "jvm_ret_addr"
									))) {
										outOfBounds = false;
									}
								}
							}
							
							if (outOfBounds) {
								throw new BadBytecodesException(
									"index out of bounds: " + iob_ex.getMessage()
								);
							}
						}
					}
						break;
					
					// iastore, lastore, fastore, dastore, aastore,
					// bastore, castore, sastore
					case 79:
					case 80:
					case 81:
					case 82:
					case 83:
					case 84:
					case 85:
					case 86:
					{
						int previous_pc = BytecodeBlock.calculatePreviousPC(bytecodes, pc);
						SpecialStack stack = s_c.getStackAt(previous_pc);
						
						// ...
						
						stack.pop(); // value
						stack.pop(); // index
						stack.pop(); // object
					}
						break;
				}
				
				localVarValues[pc] = (LocalVariableValues)(currentVarValues.clone());
			} else {
				localVarValues[pc] = null;
			}
		}
	} * * * * * * * * * * * * * * * * * * * * * * * */
	
	/**
	 *	Note:	'jvm_ret_addr' stored using 'astore' bytecode.
	 */
	public static int countMaxLocals(JavaMethod method) {
		int maxlocals = (method.isStatic()) ? 0 : /* this */ 1;
		
//++		int parameterCount = method.getParameterCount();
//++		if (maxlocals < parameterCount) {
//++			maxlocals = parameterCount;
//++		}
//++		
//++		byte[] opcodes = method.getCode();
//++		int codeLength = opcodes.length;
//++		
//++		int i = 0;
//++		while (i < codeLength) {
//++			int slot = -1;
//++			
//++			switch (opcodes[i]) {
//++				// xload_0
//++				// xstore_0
//++				case 26: case 30: case 34: case 38: case 42:
//++				case 59: case 63: case 67: case 71: case 75:
//++					slot = 0;
//++					break;
//++				
//++				// xload_1
//++				// xstore_1
//++				case 27: case 31: case 35: case 39: case 43:
//++				case 60: case 64: case 68: case 72: case 76:
//++					slot = 1;
//++					break;
//++				
//++				// xload_2
//++				// xstore_2
//++				case 28: case 32: case 36: case 40: case 44:
//++				case 61: case 65: case 69: case 73: case 77:
//++					slot = 2;
//++					break;
//++				
//++				// xload_3
//++				// xstore_3
//++				case 29: case 33: case 37: case 41: case 45:
//++				case 62: case 66: case 70: case 74: case 78:
//++					slot = 3;
//++					break;
//++				
//++				// xload
//++				// xstore
//++				case 21: case 22: case 23: case 24: case 25:
//++				case 54: case 55: case 56: case 57: case 58:
//++					slot = ByteTransformer.toUnsignedByte(opcodes[i+1]);
//++					break;
//++			}
//++			
//++			int max = slot + 1;
//++			if (maxlocals < max) maxlocals = max;
//++			
//++			i = method.nextOpcodeIndex(i);
//++		}
//++		
//++		if (maxlocals > parameterCount) {
//++			String lastVarType = findLocalVariableType(maxlocals - 1, method);
//++			if (lastVarType.equals("J") || lastVarType.equals("D")) {
//++				maxlocals++;
//++			}
//++		}
		
		return maxlocals;
	}
	
	/**
	 *	Note:	produces real local variables (not 'raw').
	 */
	public static LocalVariable[] makeLocalVariables(JavaMethod method) {
		int maxlocals = countMaxLocals(method);
		if (maxlocals <= 0) {
			return new LocalVariable[0];
		}
		
		LocalVariable[] locals = new LocalVariable[maxlocals];
		
		JavaClass owner = method.getOwnerClass();
		if (owner != null) {
			JavaConstantPool pool = owner.getConstantPool();
//////////			JavaBytecode[] bytecodes = BytecodeLocksmith.extractBytecodes(method);
			
/////////			int paramCount = method.getParameterCount();
			int paramCount = 0;
			
			int start_slot = 0;
			if (!method.isStatic()) {
				start_slot = 1;
				
				String type = ClassUtilities.parseType(owner.getClassName());
				
//==========				// 'this': scopestart = 0, scopesize = last_pc, slot = 0
//==========				locals[0] = LocalVariable.makeLocalVariable(method, 0, "this", type);
			}
			
			int slot = start_slot;
			while (slot < maxlocals) {
				// calculate 'scopestart' and 'scopesize'
				int scopestart = 0;
				int scopesize = 0;
				
				if (slot < paramCount) {
/////////////					scopesize = method.calculateLastPC() + 1;
				} else {
					// ...
					// IN PROGRESS...
					// ...
				}
				
				String type = findLocalVariableType(slot, method);
///////				locals[slot] = LocalVariable.makeLocalVariable(
/////					method, scopestart, scopesize, slot, "v" + slot, type
///				);
				
				if (type.equals("D") || type.equals("J")) {
					slot += 2;
				} else {
					slot++;
				}
			}
			
			// update pool with new [(UTF8): LocalVariableTable]
	//&&&		JavaConstantPoolElement newUTF8 = new JavaConstantPoolElement(
	//&&&			owner, /* UTF8 */ 1, "LocalVariableTable"
	//&&&		);
	//&&&		ConstantPoolManager.addConstantToPool(newUTF8, pool);
		}
		
		return locals;
	}
	
	/**
	 *	Note: the type of return address used in JVM for subrotines
	 *	is 'jvm_ret_addr' (only shown in Dump).
	 */
	private static String findLocalVariableType(int slot, JavaMethod method) {
		if (method.getOwnerClass() == null) {
			return "! null owner !";
		}
		
		// first look in parameters
		if (!method.isStatic() && (slot == 0)) {
			return 'L' + ClassUtilities.fromCommas(method.getOwnerClass().getClassName()) + ';';
		}
		
		int paramCount = 1; /*((((( WAS method.getParameterCount(); )))))*/
		if (slot < paramCount) {
			String[] parameters = { "method's parameters" };
				/////////// WAS LocalVarsCalculator.extractRawParameterList(
				///////////         method, method.getArrayOfParameters()
				///////////     );
			
			return ClassUtilities.parseType(parameters[slot]);
		}
		
		byte[] opcodes = method.getCode();
		int codeLength = opcodes.length;
		
		int pc = 0;
		while (pc < codeLength) {
			int value = -1;
			byte opcode = opcodes[pc];
			
			switch (opcode) {
				// istore_n
				case 59: case 60: case 61: case 62:
					value = (opcode - 59);
				// istore
				case 54:
					if (value == -1) {
						value = ByteTransformer.toUnsignedByte(opcodes[pc+1]);
					}
					
					if (value == slot) {
						return "I";
					}
					break;
				
				// lstore_n
				case 63: case 64: case 65: case 66:
					value = (opcode - 63);
				// lstore
				case 55:
					if (value == -1) {
						value = ByteTransformer.toUnsignedByte(opcodes[pc+1]);
					}
					
					if (value == slot) {
						return "J";
					}
					break;
				
				// fstore_n
				case 67: case 68: case 69: case 70:
					value = (opcode - 67);
				// fstore
				case 56:
					if (value == -1) {
						value = ByteTransformer.toUnsignedByte(opcodes[pc+1]);
					}
					
					if (value == slot) {
						return "F";
					}
					break;
				
				// dstore_n
				case 71: case 72: case 73: case 74:
					value = (opcode - 71);
				// dstore
				case 57:
					if (value == -1) {
						value = ByteTransformer.toUnsignedByte(opcodes[pc+1]);
					}
					
					if (value == slot) {
						return "D";
					}
					break;
				
				// astore_n
				case 75: case 76: case 77: case 78:
					value = (opcode - 75);
				// astore
				case 58:
					if (value == -1) {
						value = ByteTransformer.toUnsignedByte(opcodes[pc+1]);
					}
					
					if (value == slot) {
						return "<astore>";
					}
					break;
					
//						if (value == slot) {
// !!! THIS METHOD DOESN'T WORK PERFECTLY !!!
// USE SOMETHING BETTER, 'COS PREVIOUS MAY BE
// 'nop', ANOTHER PUSH/STORE etc.

// SINCE bytecodes are not extracted,
// this isn't working totally!!!

//							int pre_pc = BytecodeBlock.calculatePreviousPC(bytecodes, pc);
//							JavaBytecode pre_bytecode = bytecodes[pre_pc];
//							String pre_str = pre_bytecode.getBytecode();
						
//							if (pre_str.equals("new")) {
//								return ClassUtilities.parseType(pre_bytecode.getArg());
//							} else if (pre_str.equals("checkcast")) {
//								return ClassUtilities.parseType(pre_bytecode.getArg());
//							} else if (pre_str.startsWith("invoke")) {
//								return ClassUtilities.parseType(pre_bytecode.getArg());
//							} else if (pre_str.equals("aload")) {
//								int slot2 = Integer.parseInt(pre_bytecode.getArg());
//								return findLocalVariableType(slot2, method);
//							} /*else if (pre_str.equals("push")) {
//								if (pre_bytecode.getArg().charAt(0) == '\"') {
//									return "Ljava/lang/String;";
//								}
//							} */
//						}
//						break;

				// is it 'jvm_ret_addr'?
				case /* 169 ret */ -87:
					if (ByteTransformer.toUnsignedByte(opcodes[pc+1]) == slot) {
						return "jvm_ret_addr";
					}
					break;
			}
			
//////			pc = method.nextOpcodeIndex(pc);
		}
		
		return "? unknown ?";
	}
	
	/**
	 *	Raw Local Variable is always stored in ONE array cell,
	 *	even if it is <code>long</code> or <code>double</code>.
	 */
	public static LocalVariable[]
	extractRawLocalVariables(JavaMethod method, LocalVariable[] rawLocals) {
		if ((rawLocals == null) || (rawLocals.length == 0)) {
			return new LocalVariable[0];
		}
		
		final int rawCount = rawLocals.length;
		int maxlocals = countMaxLocals(method);
		
		if (maxlocals == rawCount) {
			return rawLocals;
		}
		
		LocalVariable[] locals = new LocalVariable[maxlocals];
		int pos = 0;
		
		for (int i = 0; i < rawCount; i++) {
			if (rawLocals[i] == null) {
				throw new IllegalArgumentException("can extract only raw local variables");
			}
			
			locals[pos] = rawLocals[i];
///@@@@			if (rawLocals[i].isDoubleSize()) {
///@@@@				pos += 2;
///@@@@			} else {
///@@@@				pos++;
///@@@@			}
		}
		
		if (pos < maxlocals) {
			// 'jvm_ret_addr' local variables
//%%%%%			for (int i = pos; i < maxlocals; i++) {
//%%%%%				if (findLocalVariableType(i, method).equals("jvm_ret_addr")) {
//%%%%%					locals[i] = LocalVariable.makeLocalVariable(
//%%%%%						method, /* scopestart */ 0, /* scopesize */ 0, i, "v" + i, "jvm_ret_addr"
//%%%%%					);
//%%%%%				} else {
//%%%%%					/* impossible if method's owner is not 'null' */
//%%%%%					locals[i] = null;
//%%%%%				}
//%%%%%			}
		}
		
		return locals;
	}
	
	public static String[] extractRawParameterList(JavaMethod method, String[] rawList) {
		if ((rawList == null) || (rawList.length == 0)) {
			return new String[0];
		}
		
		String[] list = rawList;
		final int rawCount = rawList.length;
		int count = rawCount;
		int correction = (method.isStatic() ? 0 : 1);
		
		for (int i = 0; i < rawCount; i++) {
			if (rawList[i] != null) {
				if (rawList[i].equals("long") || rawList[i].equals("double")) {
					correction++;
				}
			} else {
				throw new IllegalArgumentException("can extract only raw parameter list");
			}
		}
		count += correction;
		
		if (count > rawCount) {
			list = new String[count];
			int pos = (method.isStatic() ? 0 : 1);
			
			for (int i = 0; i < rawCount; i++) {
				list[pos] = rawList[i];
				
				if (rawList[i].equals("long") || rawList[i].equals("double")) {
					pos += 2;
				} else {
					pos++;
				}
			}
		}
		
		return list;
	}
	
	public LocalVariableWithValue[] getLocalVariablesAt(int pc) {
		LocalVariableValues val = this.localVarValues[pc];
		return (val == null) ? null : val.getLocalVariables();
	}
	
}


/**
 *	The local vars is calculated here.
 */

class LocalVarsCalculatorThread extends Thread {
	
	private boolean calculated;
	private JavaMethod method;
	// private LocalVariableValues[];
	
	LocalVarsCalculatorThread(JavaMethod method) {
		super("Thread-LocalVarsCalculator");
		super.setPriority(Thread.NORM_PRIORITY + 1);
		
		this.method = method;
		this.calculated = false;
		super.start();
	}
	
	boolean isCalculated() {
		return this.calculated;
	}
	
	public void run() {
		// ...
	}
	
}


/**
 *	The LocalVariableValues class wraps an array of
 *	<code>LocalVariableWithValue</code>s given from the corresponding
 *	<code>JavaMethod</code>.
 */

class LocalVariableValues extends Object implements Cloneable {
	
	private LocalVariableWithValue[] localVariables;
	
	LocalVariableValues(JavaMethod method) {
/******		LocalVariable[] methodVars = method.getLocalVariables();
		int count = methodVars.length;
		this.localVariables = new LocalVariableWithValue[count];
		
		for (int i = 0; i < count; i++) {
			if (methodVars[i] != null) {
				this.localVariables[i] = new LocalVariableWithValue(methodVars[i], null);
			} else {
				this.localVariables[i] = null;
			}
		}
*******/
	}
	
	/**
	 *	Special 'private' copy costructor.
	 *	@see  #clone
	 */
	private LocalVariableValues(LocalVariableValues copyFrom) {
		int count = copyFrom.localVariables.length;
		this.localVariables = new LocalVariableWithValue[count];
		
///////		for (int i = 0; i < count; i++) {
///////			LocalVariableWithValue current_copyFrom = copyFrom.localVariables[i];
///////			this.localVariables[i] = (current_copyFrom != null) ?
///////							(LocalVariableWithValue)(current_copyFrom.clone()) : null;
///////		}
	}
	
	/**
	 *	Returns wrapped array of <code>LocalVariableWithValue</code>s.
	 */
	LocalVariableWithValue[] getLocalVariables() {
		return this.localVariables;
	}
	
	void setLocalVariables(LocalVariableWithValue[] newVars) {
		this.localVariables = newVars;
	}
	
	LocalVariableWithValue getVariableAt(int index) throws IndexOutOfBoundsException {
		if (index < this.localVariables.length) {
			return this.localVariables[index];
		} else {
			throw new IndexOutOfBoundsException(
				index + " >= " + this.localVariables.length
			);
		}
	}
	
	Object getVariableValueAt(int index) throws IndexOutOfBoundsException {
/////		if (index < this.localVariables.length) {
/////			return this.localVariables[index].getValue();
/////		} else {
			throw new IndexOutOfBoundsException(
				index + " >= " + this.localVariables.length
			);
/////		}
	}
	
	void setVariableAt(int index, LocalVariableWithValue value) throws IndexOutOfBoundsException {
		if (index < this.localVariables.length) {
			this.localVariables[index] = value;
		} else {
			throw new IndexOutOfBoundsException(
				index + " >= " + this.localVariables.length
			);
		}
	}
	
	void setVariableValueAt(int index, Object value) throws IndexOutOfBoundsException {
		if (index < this.localVariables.length) {
////////////			this.localVariables[index].setValue(value);
		} else {
			throw new IndexOutOfBoundsException(
				index + " >= " + this.localVariables.length
			);
		}
	}
	
	/**
	 *	Creates a copy of this <code>LocalVariableValues</code> object.
	 *	
	 *	@return		a copy of this object.
	 */
	public Object clone() {
		// call special 'private' copy constructor
		return new LocalVariableValues(this);
	}
	
}
