/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.engine;

import java.io.*;
import java.util.Vector;
import java.util.EmptyStackException;

import douglas.mencken.tools.LogMonitor;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.exceptions.*;
import douglas.mencken.util.*;

import douglas.mencken.bm.storage.*;

/**
 *	<code>StackCalculator</code>
 *
 *	@version 0.80d3
 */

public class StackCalculator extends Object implements Externalizable {
	
	protected JavaMethod method;
	protected StackValuesVariant[] stackVariants;
	
	public StackCalculator(JavaMethod method) {
		this.method = method;
		this.stackVariants = null;
		
		// for further unpackaging
		new Unpackager(method.getOwnerClass());
		
///////		try {
///////			this.stackVariants = fromMethod(method);
///////		} catch (Exception exc) {
///			UsefulMessageDialogs.doErrorDialog(
////////////				exc.getClass().getName() + " (caught in StackCalculator for method '" +
////////////				method.getMethodName() + "'): " + exc.getMessage()
///			);
///////		}
	}
	
	public SpecialStack getStackAt(int pc) {
		return this.getStackAt(pc, -1);
	}
	
	/**
	 *	Note:	returns a clone of stack variant, so the original is safe.
	 */
	public SpecialStack getStackAt(int pc, int variantNumber) {
		// return 'error stack' instead of 'null'
		if (this.stackVariants == null) {
			return createStackOfErrors(2 /****** WAS method.getMaxStack() *******/);
		}
		
		// search for the first not-null variant
		if (variantNumber < 0) {
			int variantCount = this.stackVariants.length;
			for (int i = 0; i < variantCount; i++) {
				if (this.stackVariants[i] != null) {
					if (this.stackVariants[i].getStackAt(pc) != null) {
						variantNumber = i;
						break;
					}
				}
			}
			
			// if not found (variantNumber is still < 0), return 'error stack'
			if (variantNumber < 0) {
				return createStackOfErrors(1); /////////////method.getMaxStack()
			}
		}
		
		SpecialStack theStack = this.stackVariants[variantNumber].getStackAt(pc);
		return (SpecialStack)(theStack.clone());
	}
	
	public int getMaxStack() {
		return 1; /////////////////////this.method.getMaxStack();
	}
	
	/**
	 *	Writes all stack values to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(method);
		
		int length = this.stackVariants.length;
		out.writeInt(length);
		
		for (int i = 0; i < length; i++) {
			out.writeObject(this.stackVariants[i].getStack());
		}
	}
	
	/**
	 *	Reads stack values from the ObjectInput.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.method = (JavaMethod)in.readObject();
		
		int length = in.readInt();
		this.stackVariants = new StackValuesVariant[length];
		
		for (int i = 0; i < length; i++) {
			this.stackVariants[i].setStack((SpecialStack[])in.readObject());
		}
	}
	
	/**
	 *	Returns a stack filled with String "* ERROR *" 'maxstack + 1' times.
	 *
	 *	(static method)
	 */
	public static SpecialStack createStackOfErrors(int maxstack) {
		SpecialStack ret = new SpecialStack(maxstack + 1);
		for (int j = 0; j <= maxstack; j++) {
			ret.push("* ERROR *");
		}
		
		return ret;
	}
	
	/**
	 *	Fills an array of stacks with String "* ERROR *"
	 *	'maxstack + 1' times from 'pc' to 'codeLength - 1'.
	 *
	 *	(static method)
	 */
	public static void fillStacksWithErrors(SpecialStack[] stackValues, int pc,
											 int codeLength, int maxstack) {
		for (int i = pc; i < codeLength; i++) {
			stackValues[i] = new SpecialStack(maxstack + 1);
			for (int j = 0; j <= maxstack; j++) {
				stackValues[i].push("* ERROR *");
			}
		}
	}
	
	//public static StackValuesVariant[] fromMethod(JavaMethod method)
	/*****throws BadBytecodesException*****/ //{
////		byte[] opcodes = method.getCode();
////		int codeLength = opcodes.length;
////		if (codeLength == 0) {
////			return null;
////		}
////		
////		StackValuesVariant[] stackVariants = new StackValuesVariant[1];
////		SpecialStack[] currentVariant = new SpecialStack[codeLength];
////		JavaBytecode[] bytecodes = BytecodeLocksmith.extractBytecodes(method);
////		
////		int maxstack = method.getMaxStack();
////		if (maxstack == 0) maxstack = 1;
////		
////		// initial stack
////		currentVariant[0] = new SpecialStack(maxstack);
////		
////		// Because branches modifies the 'pc' values of first calculation,
////		// current 'pc' value before each branch are added to this vector.
////		Vector otherCaseStartPCs = new Vector();
////		
////		for (int pc = 0; pc < codeLength; pc++) {
////			JavaBytecode bytecode = bytecodes[pc];
////			if (bytecode != null) {
////				int opcode = ByteTransformer.toUnsignedByte(bytecode.getOpcode());
////				switch (opcode) {
////					/* Notes:
////					 * 1) 'pc+1' to skip _current_ 'pc'
////					 * 2) 'backward branch' can produce an infinite loop
////					 */
////					
////					/* ifxx */
////					case 153: case 154: case 155: case 156: case 157: case 158:
////					/* if_xcmpxx */
////					case 159: case 160: case 161: case 162: case 163: case 164:
////					case 165: case 166:
////					/* goto, goto_2 */
////					case 167: case 200:
////						int pc_inc = Integer.parseInt(bytecode.getArg());
////						if (pc_inc > 0) {
////							otherCaseStartPCs.addElement(new Integer(pc + 1));
////						}
////						break;
////				}
////			}
////		}
////		
////		calculateCodeBlock(method, bytecodes, 0, codeLength, currentVariant, 0);
////		stackVariants[0] = new StackValuesVariant(currentVariant);
////		
////		int[] otherCasePCs = ArrayUtilities.vectorToIntArray(otherCaseStartPCs);
////		if (otherCasePCs != null) {
////			int otherCaseCount = otherCasePCs.length;
////			int variantNumber = 1;
////			
////			// here: currentVariant = stackVariants[0].getStack();
////			stackVariants = new StackValuesVariant[otherCaseCount + 1];
////			stackVariants[0] = new StackValuesVariant(currentVariant);
////			
////			for (int i = 0; i < otherCaseCount; i++) {
////				currentVariant = new SpecialStack[codeLength];
////				
////				int stackPCValue = copyStackValue(
////					method, opcodes,
////					stackVariants[variantNumber-1].getStack(), currentVariant,
////					otherCasePCs[i]
////				);
////				calculateCodeBlock(
////					method, bytecodes, otherCasePCs[i],
////					codeLength, currentVariant, stackPCValue
////				);
////				
////				stackVariants[variantNumber] = new StackValuesVariant(currentVariant);
////				variantNumber++;
////			}
////		}
////		
////		return stackVariants;
////	}
	
	/**
	 *	Copy the stack at previous pc from 'srcStack' to 'dstStack'.
	 *
	 *	@return		previous pc value.
	 */
	protected static int copyStackValue(JavaMethod method, final byte[] opcodes,
										SpecialStack[] srcStack,
										SpecialStack[] dstStack,
										int startPC) {
		int previousIndex = -1; /* WAS ==== JavaMethod.previousOpcodeIndex(opcodes, startPC); ==== **/
		if (previousIndex < 0) previousIndex = 0;
		
		SpecialStack stack = (SpecialStack)(srcStack[previousIndex].clone());
		
		// avoid copy some values from error stacks
		if (!stack.isEmpty() && stack.peek().equals("* ERROR *")) {
			stack = null;
		}
		
		dstStack[previousIndex] = stack;
		return previousIndex;
	}
	
	
	
	/**
	 *	Note:	before calling this method, make sure that
	 *			stackValues[stackPCValue] is copied with 'copyStackValue'.
	 *
	 *	@see	copyStackValue
	 */
////	protected static void calculateCodeBlock(JavaMethod method, final JavaBytecode[] bytecodes,
////											 int startPC, int endPC,
////											 SpecialStack[] stackValues, int stackPCValue)
////	throws BadBytecodesException {
////		LocalVariable[] locals = method.getLocalVariables();
////		boolean hasLocals = (locals.length > 0);
////		
////		TryCatchFinallyBlock[] catches = method.getTryCatchBlocks();
////		int catchCount = catches.length;
////		int[] catchHandlerPCs = null;
////		if (catchCount > 0) {
////			catchHandlerPCs = new int[catchCount];
////			for (int i = 0; i < catchCount; i++) {
////				catchHandlerPCs[i] = catches[i].getHandlerPCValue();
////			}
////		}
////		
////		int pc = startPC;
////		Unpackager unpackager = Unpackager.getCurrentUnpackager();
////		SpecialStack stack = (SpecialStack)(stackValues[stackPCValue].clone());
////		
////		try {
////			for (; pc < endPC; pc++) {
////				JavaBytecode bytecode = bytecodes[pc];
////				if (bytecode != null) {
////					if (catchHandlerPCs != null) {
////						// for catch handler push the 'throwable' object
////						int index = ArrayUtilities.findMatchingElement(catchHandlerPCs, pc);
////						if (index != -1) {
////							stack.push("t" + index);
////						}
////					}
////					
////					int opcode = ByteTransformer.toUnsignedByte(bytecode.getOpcode());
////					
////					switch (opcode) {
////						case 1:
////							stack.push("null");
////							break;
////						case 2:
////							stack.push(bytecode.getArg());
////							break;
////						case 9: case 10:
////							stack.push(String.valueOf(opcode - 9) + 'L');
////							break;
////						case 11: case 12: case 13:
////							stack.push(String.valueOf(opcode - 11) + ".0f");
////							break;
////						case 14: case 15:
////							stack.push(String.valueOf(opcode - 14) + ".0d");
////							break;
////						
////						// push
////						case 16:
////							stack.push(bytecode.getArg());
////							break;
////						
////						// iload, lload, fload, dload
////						case 21:
////						case 22:
////						case 23:
////						case 24:
////						{
////							int index = Integer.parseInt(bytecode.getArg());
////							String toPush = null;
////							
////							if (hasLocals) {
////								try {
////									toPush = locals[index].getVariableName();
////								} catch (ArrayIndexOutOfBoundsException exc) {}
////							}
////							
////							if (toPush == null) {
////								toPush = 'v' + Integer.toString(index);
////							}
////							
////							stack.push(toPush);
////						}
////							break;
////						// aload
////						case 25:
////						{
////							int index = Integer.parseInt(bytecode.getArg());
////							String toPush = null;
////							
////							if ((index == 0) && (!method.isStatic())) {
////								toPush = "this";
////							} else {
////								if (hasLocals) {
////									try {
////										toPush = locals[index].getVariableName();
////									} catch (ArrayIndexOutOfBoundsException exc) {}
////								}
////							}
////							
////							if (toPush == null) {
////								toPush = 'v' + Integer.toString(index);
////							}
////							
////							stack.push(toPush);
////						}
////							break;
////						
////						// iaload, laload, faload, daload, aaload,
////						// baload, caload, saload
////						case 46:
////						case 47:
////						case 48:
////						case 49:
////						case 50:
////						case 51:
////						case 52:
////						case 53:
////						{
////							String index = (String)stack.pop();
////							String obj = (String)stack.pop();
////							stack.push(obj + '[' + index + ']');
////							
////							break;
////						}
////						
////						// istore, lstore, fstore, dstore, astore
////						case 54:
////						case 55:
////						case 56:
////						case 57:
////						case 58:
////							stack.pop(); // value
////							break;
////						
////						// iastore, lastore, fastore, dastore, aastore,
////						// bastore, castore, sastore
////						case 79:
////						case 80:
////						case 81:
////						case 82:
////						case 83:
////						case 84:
////						case 85:
////						case 86:
////							stack.pop(); // value
////							stack.pop(); // index
////							stack.pop(); // object
////							break;
////						
////						// pop
////						case 87:
////							stack.pop();
////							break;
////						// pop2
////						case 88:
////							stack.pop();
////							stack.pop();
////							break;
////						// dup
////						case 89:
////							stack.dup();
////							break;
////						// dup2
////						case 92:
////						{
////							String v1 = (String)stack.pop();
////							String v2 = (String)stack.pop();
////							stack.push(v2);
////							stack.push(v1);
////							stack.push(v2);
////							stack.push(v1);
////						}
////							break;
////						// dup_x1
////						/* v1  //  v1 *
////						 * v2  //  v2 *
////						 * v3  //  v1 *
////						 * v4  //  v3 *
////						 * ..  //  v4 */
////						case 90:
////						{
////							String v1 = (String)stack.pop();
////							String v2 = (String)stack.pop();
////							stack.push(v1);
////							stack.push(v2);
////							stack.push(v1);
////						}
////							break;
////						// dup_x2
////						/* v1  //  v1 *
////						 * v2  //  v2 *
////						 * v3  //  v3 *
////						 * v4  //  v1 *
////						 * ..  //  v4 */
////						case 91:
////						{
////							String v1 = (String)stack.pop();
////							String v2 = (String)stack.pop();
////							String v3 = (String)stack.pop();
////							stack.push(v1);
////							stack.push(v3);
////							stack.push(v2);
////							stack.push(v1);
////						}
////							break;
////						// dup2_x1
////						/* v1  //  v1 *
////						 * v2  //  v2 *
////						 * v3  //  v3 *
////						 * v4  //  v1 *
////						 * v5  //  v2 *
////						 * ..  //  v4 *
////						 * ..  //  v5 */
////						case 93:
////						{
////							String v1 = (String)stack.pop();
////							String v2 = (String)stack.pop();
////							String v3 = (String)stack.pop();
////							stack.push(v2);
////							stack.push(v1);
////							stack.push(v3);
////							stack.push(v2);
////							stack.push(v1);
////						}
////							break;
////						// dup2_x2
////						/* v1  //  v1 *
////						 * v2  //  v2 *
////						 * v3  //  v3 *
////						 * v4  //  v4 *
////						 * v5  //  v1 *
////						 * ..  //  v2 *
////						 * ..  //  v5 */
////						case 94:
////						{
////							String v1 = (String)stack.pop();
////							String v2 = (String)stack.pop();
////							String v3 = (String)stack.pop();
////							String v4 = (String)stack.pop();
////							stack.push(v2);
////							stack.push(v1);
////							stack.push(v4);
////							stack.push(v3);
////							stack.push(v2);
////							stack.push(v1);
////						}
////							break;
////						// swap
////						case 95:
////							stack.swap();
////							break;
////						
////						// xadd
////						case 96:
////						case 97:
////						case 98:
////						case 99:
////						{
////							String value1 = (String)stack.pop();
////							String value2 = (String)stack.pop();
////							stack.push(value2 + " + " + value1);
////							break;
////						}
////						
////						// xsub
////						case 100:
////						case 101:
////						case 102:
////						case 103:
////						{
////							String value1 = (String)stack.pop();
////							String value2 = (String)stack.pop();
////							stack.push(value2 + " - " + value1);
////							break;
////						}
////						
////						// xmul
////						case 104:
////						case 105:
////						case 106:
////						case 107:
////						{
////							String value1 = (String)stack.pop();
////							String value2 = (String)stack.pop();
////							stack.push(value2 + '*' + value1);
////							break;
////						}
////						
////						// xdiv
////						case 108:
////						case 109:
////						case 110:
////						case 111:
////						{
////							String value1 = (String)stack.pop();
////							String value2 = (String)stack.pop();
////							stack.push(value2 + '/' + value1);
////							break;
////						}
////						
////						// xrem
////						case 112:
////						case 113:
////						case 114:
////						case 115:
////						{
////							String value1 = (String)stack.pop();
////							String value2 = (String)stack.pop();
////							stack.push(value2 + '%' + value1);
////							break;
////						}
////						
////						// xneg
////						case 116:
////						case 117:
////						case 118:
////						case 119:
////						{
////							String top = (String)stack.pop();
////							if (top.startsWith("-")) {
////								top = StringUtilities.getAfter(top, '-');
////							} else {
////								top = "-" + top;
////							}
////							
////							stack.push(top);
////							break;
////						}
////						
////						// ishl, lshl
////						case 120:
////						case 121:
////						{
////							String shiftCount = (String)stack.pop();
////							String value = (String)stack.pop();
////							stack.push(value + " << " + shiftCount);
////							break;
////						}
////						
////						// ishr, lshr
////						case 122:
////						case 123:
////						{
////							String shiftCount = (String)stack.pop();
////							String value = (String)stack.pop();
////							stack.push(value + " >> " + shiftCount);
////							break;
////						}
////						
////						// iushr, lushr
////						case 124:
////						case 125:
////						{
////							String shiftCount = (String)stack.pop();
////							String value = (String)stack.pop();
////							stack.push(value + " >>> " + shiftCount);
////							break;
////						}
////						
////						// iand, land
////						case 126:
////						case 127:
////						{
////							String value1 = (String)stack.pop();
////							String value2 = (String)stack.pop();
////							stack.push(value2 + " & " + value1);
////							break;
////						}
////						
////						// ior, lor
////						case 128:
////						case 129:
////						{
////							String value1 = (String)stack.pop();
////							String value2 = (String)stack.pop();
////							stack.push(value2 + " | " + value1);
////							break;
////						}
////						
////						// ixor, lxor
////						case 130:
////						case 131:
////						{
////							String value1 = (String)stack.pop();
////							String value2 = (String)stack.pop();
////							stack.push(value2 + " ^ " + value1);
////							break;
////						}
////						
////						case 136:
////						case 139:
////						case 142:
////						{
////							String value = (String)stack.pop();
////							stack.push("(int)(" + value + ')');
////							break;
////						}
////						case 133:
////						case 140:
////						case 143:
////						{
////							String value = (String)stack.pop();
////							stack.push("(long)(" + value + ')');
////							break;
////						}
////						case 134:
////						case 137:
////						case 144:
////						{
////							String value = (String)stack.pop();
////							stack.push("(float)(" + value + ')');
////							break;
////						}
////						case 135:
////						case 138:
////						case 141:
////						{
////							String value = (String)stack.pop();
////							stack.push("(double)(" + value + ')');
////							break;
////						}
////						case 145:
////						{
////							String value = (String)stack.pop();
////							stack.push("(byte)(" + value + ')');
////							break;
////						}
////						case 146:
////						{
////							String value = (String)stack.pop();
////							stack.push("(char)(" + value + ')');
////							break;
////						}
////						case 147:
////						{
////							String value = (String)stack.pop();
////							stack.push("(short)(" + value + ')');
////							break;
////						}
////						
////						// xcmpx
////						case 148: case 149: case 150: case 151: case 152:
////						{
////							String value2 = (String)stack.pop();
////							String value1 = (String)stack.pop();
////							stack.push(value1 + " cmp " + value2);
////							
////							break;
////						}
////						
////						// ifxx
////						case 153:
////						case 154:
////						case 155:
////						case 156:
////						case 157:
////						case 158:
////						{
////							stack.pop();
////							stackValues[pc] = (SpecialStack)(stack.clone());
////							
////							int pc_inc = Integer.parseInt(bytecode.getArg());
////							if (pc_inc > 0) {
////								// -1 because loop incrementes 'pc' too
////								pc += pc_inc - 1;
////							}
////						}
////							break;
////						
////						// if_icmpxx, if_acmpeq, if_acmpne
////						case 159: case 160: case 161: case 162: case 163: case 164:
////						case 165: case 166:
////						{
////							stack.pop(); // value2
////							stack.pop(); // value1
////							stackValues[pc] = (SpecialStack)(stack.clone());
////							
////							int pc_inc = Integer.parseInt(bytecode.getArg());
////							if (pc_inc > 0) {
////								pc += pc_inc - 1;
////							}
////						}
////							break;
////						
////						// goto, goto_2
////						case 167:
////						case 200:
////						{
////							stackValues[pc] = (SpecialStack)(stack.clone());
////							
////							int pc_inc = Integer.parseInt(bytecode.getArg());
////							if (pc_inc > 0) {
////								pc += pc_inc - 1;
////							}
////						}
////							break;
////						
////						// jsr, jsr_w
////						case 168:
////						case 201:
////						{
////							int jsr_pc = BytecodeBlock.calculateNextPC(bytecodes, pc, endPC);
////							stack.push(String.valueOf(jsr_pc));
////							break;
////						}
////						
////						// xswitch
////						case 170:
////						case 171:
////							stack.pop();
////							break;
////						
////						// xreturn
////						case 172:
////						case 173:
////						case 174:
////						case 175:
////						case 176:
////							stack.pop(); // return value
////							break;
////						
////						// getstatic
////						case 178:
////						{
////							String className = unpackager.unpackage(bytecode.getArg2());
////							String fieldName = unpackager.unpackage(bytecode.getArg3());
////							stack.push(className + "." + fieldName);
////							break;
////						}
////						
////						// putstatic
////						case 179:
////							stack.pop(); // value
////							break;
////						
////						// getfield
////						case 180:
////						{
////							String fieldName = unpackager.unpackage(bytecode.getArg3());
////							stack.push(stack.pop() + "." + fieldName);
////							break;
////						}
////						
////						// putfield
////						case 181:
////							stack.pop(); // value
////							stack.pop(); // varName
////							break;
////						
////						// invokevirtual
////						case 182:
////						// invokenonvirtual
////						case 183:
////						// invokestatic
////						case 184:
////						// invokeinterface
////						case 185:
////						{
////							String returnType = bytecode.getArg();
////							String className = unpackager.unpackage(bytecode.getArg2());
////							String methodName = unpackager.unpackage(bytecode.getArg3());
////							String[] params = bytecode.getAdditionalArgs();
////							
////							StringBuffer buf = new StringBuffer();
////							if (returnType.equals("void")) {
////								buf.append(" (void) ");
////							}
////							
////							boolean invokesConstructor = (methodName.equals("<init>"));
////							
////							boolean invokenonvirtual = (opcode == 183);
////							boolean invokestatic = (opcode == 184);
////							boolean invokeinterface = (opcode == 185);
////							
////							int paramCount = params.length;
////							int startFrom = (invokestatic) ? 0 : 1;
////							int count = paramCount + startFrom;
////							
////							String[] stackArray = new String[count];
////							for (int i = count-1; i >= 0; i--) {
////								stackArray[i] = (String)stack.pop();
////							}
////							
////							if (!invokestatic && (stackArray[0].equals("this"))) {
////								buf.append(unpackager.castThis(className));
////								
////								if (!invokesConstructor) {
////									buf.append(".");
////									buf.append(methodName);
////								}
////							} else {
////								if (invokestatic) {
////									buf.append(className);
////								} else {
////									buf.append(stackArray[0]);
////								}
////								buf.append(".");
////								buf.append(methodName);
////							}
////							
////							buf.append("(");
////							for (int i = startFrom; i < count; i++) {
////								String value = stackArray[i];
////								if (params[i - startFrom].equals("boolean")) {
////									value = Unpackager.toBooleanValue(value);
////								}
////								buf.append(value);
////								
////								if ((i+1) != count) {
////									buf.append(", ");
////								}
////							}
////							buf.append(")");
////							
////							stack.push(buf.toString());
////							break;
////						}
////						
////						// new
////						case 187:
////						{
////							String className = unpackager.unpackage(bytecode.getArg());
////							String pushWhat = "new " + className;
////							
////							if (bytecode.getAdditionalArgs() != null) {
////								StringBuffer buf = new StringBuffer();
////								
////								buf.append("new ");
////								buf.append(className);
////								buf.append("(");
////								
////								String[] params = bytecode.getAdditionalArgs();
////								int paramCount = params.length;
////								
////								String[] stackArray = new String[paramCount];
////								for (int i = paramCount-1; i >= 0; i--) {
////									stackArray[i] = (String)stack.pop();
////								}
////								
////								for (int i = 0; i < paramCount; i++) {
////									String value = stackArray[i];
////									if ((params[i] != null) && params[i].equals("boolean")) {
////										value = Unpackager.toBooleanValue(value);
////									}
////									buf.append(value);
////									
////									if ((i+1) != paramCount) {
////										buf.append(", ");
////									}
////								}
////								buf.append(")");
////								
////								// pop '187 new <class_name>'
////								String _187_new_ = (String)stack.pop();
////								String _187_new_2_ = null;
////								// pop the 'dup'licated '187 new <class_name>' (if any)
////								if (!stack.isEmpty()) {
////									_187_new_2_ = (String)stack.peek();
////								}
////								if ((_187_new_2_ != null) && _187_new_.equals(_187_new_2_)) {
////									stack.pop();
////								}
////								
////								pushWhat = buf.toString();
////							}
////							
////							stack.push(pushWhat);
////							break;
////						}
////						
////						// newarray, anewarray
////						case 188:
////						case 189:
////						{
////							String numberOfElements = (String)stack.pop();
////							String arg2 = bytecode.getArg2();
////							StringBuffer additionalDimensions = new StringBuffer();
////							
////							if (arg2.startsWith("<")) {
////								arg2 = StringUtilities.removeFirstLastSymbols(arg2);
////								
////								// for things like 'anewarray [Ljava.lang.String;'
////								if (arg2.charAt(0) == '[') {
////									int numberOfAdditionalDimensions =
////											StringUtilities.countSymbol(arg2, '[');
////									arg2 = StringUtilities.getBefore(
////										ClassUtilities.describeJVMType(arg2), '['
////									);
////									
////									for (int i = 0; i < numberOfAdditionalDimensions; i++) {
////										additionalDimensions.append("[]");
////									}
////								}
////							}
////							
////							StringBuffer buf = new StringBuffer();
////							buf.append("new ").append(unpackager.unpackage(arg2));
////							buf.append("[").append(numberOfElements).append("]");
////							buf.append(additionalDimensions);
////							
////							stack.push(buf.toString());
////							break;
////						}
////						
////						// arraylength
////						case 190:
////						{
////							String pushWhat = stack.pop() + ".length";
////							stack.push(pushWhat);
////						}
////							break;
////						
////						// athrow
////						case 191:
////							stack.pop();
////							break;
////						
////						// checkcast
////						case 192:
////						{
////							String castClass = unpackager.unpackage(bytecode.getArg());
////							String pushWhat = '(' + castClass + ')' + stack.pop();
////							stack.push(pushWhat);
////						}
////							break;
////						// instanceof
////						case 193:
////							String type = unpackager.unpackage(bytecode.getArg());
////							stack.push((String)stack.pop() + " instanceof " + type);
////							break;
////						
////						// multianewarray
////						case 197:
////						{
////							String arg2 = bytecode.getArg2();
////							StringBuffer buf = new StringBuffer();
////							
////							int numberOfAdditionalDimensions =
////								StringUtilities.countSymbol(arg2, '[');
////							arg2 = ClassUtilities.describeJVMType(
////								StringUtilities.removeFirstLastSymbols(arg2)
////							);
////							
////							buf.append("new ");
////							buf.append(unpackager.unpackage(
////								StringUtilities.getBefore(arg2, '[')
////							));
////							
////							int numberOfDimensions = Integer.parseInt(bytecode.getArg3());
////							numberOfAdditionalDimensions -= numberOfDimensions;
////							
////							String[] stackArray = new String[numberOfDimensions];
////							for (int i = numberOfDimensions-1; i >= 0; i--) {
////								stackArray[i] = (String)stack.pop();
////							}
////							
////							for (int i = 0; i < numberOfDimensions; i++) {
////								buf.append('[');
////								buf.append(stackArray[i]);
////								buf.append(']');
////							}
////							for (int i = 0; i < numberOfAdditionalDimensions; i++) {
////								buf.append("[]");
////							}
////							
////							stack.push(buf.toString());
////							break;
////						}
////						
////						// ifnull, ifnonull
////						case 198:
////						case 199:
////							stack.pop();
////							break;
////						
////						// "stack-free" bytecodes
////						case 132:
////						case 169: case 209:
////						case 177:
////						/* newfromname */ case 186: 
////						case 194: case 195:
////						case 202:
////							break;
////						
////						default:
////							throw new BadBytecodesException("Unknown opcode: " + opcode);
////					}
////					
////					stackValues[pc] = (SpecialStack)(stack.clone());
////				} else {
////					stackValues[pc] = null;
////				}
////			}
////		} catch (EmptyStackException ese) {
////			if (BMPreferencesManager.getShowLog()) {
////				LogMonitor.addToCurrentLog(
////					"stack is empty: method '" + /*method.getNameWithParameters*/ method.getMethodName() +
////					"', pc = " + pc
////				);
////			}
////			fillStacksWithErrors(stackValues, pc, method.getCodeLength(), method.getMaxStack());
////		} catch (StackOverflowError soe) {
////			if (BMPreferencesManager.getShowLog()) {
////				LogMonitor.addToCurrentLog(
////					"stack overflow: method '" + /*method.getNameWithParameters*/ method.getMethodName() +
////					"', pc = " + pc
////				);
////			}
////			fillStacksWithErrors(stackValues, pc, method.getCodeLength(), method.getMaxStack());
////		} catch (Exception exc) {
////			throw new BadBytecodesException(exc.getClass().getName() + " caught");
////		}
////	}
	
	/**
	 *	(static method)
	 */
	public static int countMaxStack(JavaMethod method) {
		int[] catchHandlerPCs = null;
		
//////		TryCatchFinallyBlock[] catches = method.getTryCatchBlocks();
//////		int catchCount = catches.length;
//////		if (catchCount > 0) {
//////			catchHandlerPCs = new int[catchCount];
//////			for (int i = 0; i < catchCount; i++) {
//////				catchHandlerPCs[i] = catches[i].getHandlerPCValue();
//////			}
//////		}
		
//////		JavaBytecode[] bytecodes = method.getBytecodes();
//////		int codeLength = bytecodes.length;
		
		// (1) pc value = 0, (1) stack top = 0 [, (2) pc value, (2) stack top [, ...]]
//////		Vector pcAndStackTopValues = new Vector();
//////		pcAndStackTopValues.addElement(new Integer(0));
//////		pcAndStackTopValues.addElement(new Integer(0));
		
		int maxstack = 0;
		int stacktop = 0;
		
		int valueCount = 1;
		int pcIncrement = 0;
		
//###		for (int pos = 0; pos < valueCount; pos++) {
//###			int startpc = ((Integer)(pcAndStackTopValues.elementAt(pos*2))).intValue();
//###			stacktop = ((Integer)(pcAndStackTopValues.elementAt(pos*2 + 1))).intValue();
//###			
//###			for (int pc = startpc; pc < codeLength; pc++) {
//###				JavaBytecode bytecode = bytecodes[pc];
//###				if (bytecode != null) {
//###					// only forward branches is valid here
//###					if (pcIncrement > 0) {
//###						int nextpc = pc - pcIncrement + 1;
//###						boolean isUnique = true;
//###						
//###						for (int n = 0; n < valueCount; n++) {
//###							Integer currentValue = (Integer)(pcAndStackTopValues.elementAt(n*2));
//###							if (currentValue.intValue() == nextpc) {
//###								isUnique = false;
//###								break;
//###							}
//###						}
//###						
//###						// add only if pc value is unique
//###						if (isUnique) {
//###							pcAndStackTopValues.addElement(new Integer(nextpc));
//###							pcAndStackTopValues.addElement(new Integer(stacktop));
//###							
//###							valueCount++;
//###						}
//###					}
//###					
//###					pcIncrement = 0;
//###					
//###					// push 'Throwable' object if necessary
//###					if (catchHandlerPCs != null) {
//###						int index = ArrayUtilities.findMatchingElement(catchHandlerPCs, pc);
//###						if (index != -1) {
//###							stacktop++;
//###						}
//###					}
//###					
//###					switch (bytecode.getOpcode()) {
//###						// aconst_null
//###						// xconst_n
//###						// bipush, sipush
//###						// ldc1, ldc2, ldc2w
//###						case 1:
//###						case 2: case 3: case 4: case 5: case 6: case 7: case 8:
//###						case 9: case 10:
//###						case 11: case 12: case 13:
//###						case 14: case 15:
//###						case 16: case 17:
//###						case 18: case 19: case 20:
//###							stacktop++;
//###							break;
//###						
//###						// xload, xload_n
//###						case 21: case 22: case 23: case 24: case 25:
//###						case 26: case 27: case 28: case 29:
//###						case 30: case 31: case 32: case 33:
//###						case 34: case 35: case 36: case 37:
//###						case 38: case 39: case 40: case 41:
//###						case 42: case 43: case 44: case 45:
//###							stacktop++;
//###							break;
//###						
//###						// (ilfdabcs)aload
//###						case 46: case 47: case 48: case 49:
//###						case 50: case 51: case 52: case 53:
//###							// pop (index), pop (array), push (element)
//###							stacktop--;
//###							break;
//###						
//###						// xstore, xstore_n
//###						case 54: case 55: case 56: case 57: case 58:
//###						case 59: case 60: case 61: case 62:
//###						case 63: case 64: case 65: case 66:
//###						case 67: case 68: case 69: case 70:
//###						case 71: case 72: case 73: case 74:
//###						case 75: case 76: case 77: case 78:
//###							stacktop--;
//###							break;
//###						
//###						// (ilfdabcs)astore
//###						case 79: case 80: case 81: case 82:
//###						case 83: case 84: case 85: case 86:
//###							// pop (value), pop (index), pop (array)
//###							stacktop -= 3;
//###							break;
//###						
//###						// pop
//###						case 87:
//###							stacktop--;
//###							break;
//###						// pop2
//###						case 88:
//###							stacktop -= 2;
//###							break;
//###						// dup, dup_x1, dup_x2
//###						case 89: case 90: case 91:
//###							stacktop++;
//###							break;
//###						// dup2, dup2_x1, dup2_x2
//###						case 92: case 93: case 94:
//###							stacktop += 2;
//###							break;
//###						
//###						// xadd, xsub, xmul, xdiv, xrem
//###						// (il)and, (il)or, (il)xor
//###						case  96: case  97: case  98: case  99:
//###						case 100: case 101: case 102: case 103:
//###						case 104: case 105: case 106: case 107:
//###						case 108: case 109: case 110: case 111:
//###						case 112: case 113: case 114: case 115:
//###						
//###						case 126: case 127:
//###						case /* 128 */ -128: case /* 129 */ -127:
//###						case /* 130 */ -126: case /* 131 */ -125:
//###							// pop (value1), pop (value2), push (result)
//###							stacktop--;
//###							break;
//###						
//###						// xshx
//###						case 120: case 121:
//###						case 122: case 123:
//###						case 124: case 125:
//###							// pop (shift count), pop (value), push (result)
//###							stacktop--;
//###							break;
//###						
//###						// lcmp, fcmp(lq), dcmp(lq)
//###						case /* 148 */ -108:
//###						case /* 149 */ -107: case /* 150 */ -106:
//###						case /* 151 */ -105: case /* 152 */ -104:
//###							// pop (value1), pop (value2), push (result)
//###							stacktop--;
//###							break;
//###						
//###						// ifxx
//###						// ifnull, ifnonull
//###						case -103: case -102: case -101: case -100: case -99: case -98:
//###						case -58: case -57:
//###							stacktop--;
//###							
//###							pcIncrement = Integer.parseInt(bytecode.getArg());
//###							if (pcIncrement > 0) {
//###								pc += pcIncrement - 1;
//###							}
//###							
//###							break;
//###						
//###						// if_icmpxx
//###						// if_acmpxx
//###						case -97: case -96: case -95: case -94: case -93: case -92:
//###						case -91: case -90:
//###							stacktop -= 2;
//###							pcIncrement = Integer.parseInt(bytecode.getArg());
//###							if (pcIncrement > 0) {
//###								pc += pcIncrement - 1;
//###							}
//###							
//###							break;
//###						
//###						/* goto, goto_2 */
//###						case /* 167 */ -89: case /* 200 */ -56:
//###							pcIncrement = Integer.parseInt(bytecode.getArg());
//###							if (pcIncrement > 0) {
//###								pc += pcIncrement - 1;
//###							}
//###							
//###							break;
//###						
//###						// jsr, jsr_w
//###						case /* 168 */ -88: case /* 201 */ -55:
//###							// push (next pc)
//###							stacktop++;
//###							break;
//###						
//###						// xswitch, xreturn
//###						case /* 170 */ -86: case /* 171 */ -85:
//###						case -84: case -83: case -82: case -81: case -80:
//###							stacktop--;
//###							break;
//###						
//###						// getstatic
//###						case /* 178 */ -78:
//###							stacktop++;
//###							break;
//###						// putstatic
//###						case /* 179 */ -77:
//###							stacktop--;
//###							break;
//###						// putfield
//###						case /* 181 */ -75:
//###							// pop (value), pop (varName)
//###							stacktop -= 2;
//###							break;
//###						
//###						// invokevirtual
//###						case /* 182 */ -74:
//###						// invokenonvirtual
//###						case /* 183 */ -73:
//###						// invokestatic
//###						case /* 184 */ -72:
//###						// invokeinterface
//###						case /* 185 */ -71:
//###							String arg2 = StringUtilities.removeFirstLastSymbols(bytecode.getArg2());
//###							String parameters = StringUtilities.splitByChar(arg2, '(')[1];
//###							
//###							int paramCount = ClassUtilities.countParameters(parameters, false);
//###							int correction = (bytecode.getOpcode() == -72 /* 184 */) ? 0 : 1;
//###							int popCount = paramCount + correction;
//###							stacktop -= popCount;
//###							
//###							String returnType = StringUtilities.getBefore(arg2, ' ');
//###							if (!returnType.equals("void")) {
//###								stacktop++; // push (result)
//###							}
//###							
//###							break;
//###						
//###						// new
//###						case /* 187 */ -69:
//###							stacktop++;
//###							break;
//###						
//###						// athrow
//###						case /* 191 */ -65:
//###							stacktop--;
//###							break;
//###						
//###						// multianewarray
//###						case /* 197 */ -59:
//###							int numberOfDimensions = Integer.parseInt(bytecode.getArg3());
//###							
//###							stacktop -= numberOfDimensions;
//###							stacktop++; // push (array)
//###							break;
//###						
//###						default:
//###							// do nothing
//###							break;
//###					}
//###					
//###					if (maxstack < stacktop) maxstack = stacktop;
//###				}
//###			}
//###		}
		
		return maxstack;
	}
	
}


/**
 *	The stack is calculated here.
 */

class StackCalculatorThread extends Thread {
	
	private boolean calculated;
	private JavaMethod method;
	// private StackValuesVariant[] result;
	
	StackCalculatorThread(JavaMethod method) {
		super("Thread-StackCalculator");
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
 *	The StackValuesVariant class wraps an array of <code>SpecialStack</code>s.
 */

class StackValuesVariant extends Object /* implements Cloneable */ {
	
	private SpecialStack[] stackValues;
	
	StackValuesVariant(SpecialStack[] stackValues) {
		super();
		this.stackValues = stackValues;
	}
	
	SpecialStack getStackAt(int pc) {
		if (stackValues == null) {
			throw new NullPointerException("stackValues = null");
		}
		if (pc >= stackValues.length) {
			throw new ArrayIndexOutOfBoundsException("'pc' is out of range");
		}
		
		return this.stackValues[pc];
	}
	
	void setStackAt(int pc, SpecialStack newValue) {
		this.stackValues[pc] = newValue;
	}
	
	SpecialStack[] getStack() {
		return this.stackValues;
	}
	
	void setStack(SpecialStack[] newStackValues) {
		this.stackValues = newStackValues;
	}
	
}
