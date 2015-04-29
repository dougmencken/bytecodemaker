// ===========================================================================
// BlockDecompiler.java (part of douglas.mencken.bm.decompiler package)
// ===========================================================================

package douglas.mencken.bm.decompiler;

import java.util.Vector;
import java.util.EmptyStackException;

import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.util.ByteTransformer;
import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.SpecialStack;
import douglas.mencken.bm.storage.JavaMethod;
//////////import douglas.mencken.bm.storage.BytecodeItem;
//////////import douglas.mencken.bm.storage.BytecodeBlock;
//////////import douglas.mencken.bm.storage.LocalVariable;

import douglas.mencken.bm.engine.Unpackager;
import douglas.mencken.bm.engine.StackCalculator;

/**
 *	<code>BlockDecompiler</code>
 *
 *	@version 1.05b3
 */

public class BlockDecompiler extends Object implements Decompiler {
	
	private JavaMethod method;
//////////	private BytecodeItem[] bytecodes;
	private int startPC;
	private int endPC;
/////////	private LocalVariable[] locals;
	
	protected String spaceString;
	protected String oneSpaceString;
	protected String[] lines;
	
	// -----------------------------------------------------------------------------
	
	/**
	 *	The constructor.
	 */
/***************
	public BlockDecompiler(JavaMethod method, BytecodeBlock block,
							int startPC, int endPC, int level, int spaceCount,
							boolean[] declaredVars) {
		this.bytecodes = block.getBytecodes();
		
		if ((this.bytecodes == null) || (this.bytecodes.length == 0)) {
			UsefulMessageDialogs.sayAboutInternalError(
				"BlockDecompiler: attempt to decompile 'null' or empty bytecode block"
			);
			return;
		}
		
		this.method = method;
////////////		this.locals = method.getLocalVariables();
		
		StringBuffer spaceBuf = new StringBuffer();
		StringUtilities.addSpaces(spaceBuf, level*spaceCount);
		this.spaceString = spaceBuf.toString();
		
		spaceBuf = new StringBuffer();
		StringUtilities.addSpaces(spaceBuf, spaceCount);
		this.oneSpaceString = spaceBuf.toString();
		
		this.startPC = startPC;
		this.endPC = endPC;
		
// System.out.println(this.toString());
		
		if (endPC > startPC) {
			try {
				decompileBlock(method.getStackCalculator(), startPC, endPC, declaredVars);
			} catch (InternalError err) {
				String message = err.getMessage();
				UsefulMessageDialogs.sayAboutInternalError(message);
				throw new InternalError(message);
			}
		}
	}
*************************/
	
	protected void decompileBlock(StackCalculator calculator,
									int startPC, int endPC, boolean[] declaredVars) {
		Vector linesVector = new Vector();
		// initial stack
		SpecialStack stack = new SpecialStack(calculator.getMaxStack());
		Unpackager unpackager = Unpackager.getCurrentUnpackager();
		
		int extraPC = -1;
		String extraPush = "";
		
try {
		for (int pc = startPC; pc <= endPC; pc++) {
////////////////////			BytecodeItem bytecode = bytecodes[pc];
			
////////////////////			if (bytecode != null) {
// System.out.println("pc = " + pc + ", stack = " + stack);
// System.out.println("--> PROCESSING: " + bytecode);
				
///				int opcode = ByteTransformer.toUnsignedByte(bytecode.getOpcode());
				int opcode = 1;

				switch (opcode) {
					// putstatic
					case 179:
					{
						StringBuffer buf = new StringBuffer();
						
///////////						String type = unpackager.unpackage(bytecode.getArg());
///////////						String className = unpackager.unpackage(bytecode.getArg2());
///////////						String fieldName = bytecode.getArg3();
						
///////////						buf.append(className);
///////////						buf.append(".");
///////////						buf.append(fieldName);
///////////						buf.append(" = ");
						
						String value = (String)stack.pop();
			/////			if (type.equals("boolean")) {
			///////				value = Unpackager.toBooleanValue(value);
			/////			}
						buf.append(value);
						
						linesVector.addElement(buf.toString());
					}
						break;
					
					// putfield
					case 181:
					{
						StringBuffer buf = new StringBuffer();
						
	///////					String type = unpackager.unpackage(bytecode.getArg());
	///////					String className = unpackager.unpackage(bytecode.getArg2());
	///////					String fieldName = bytecode.getArg3();
						
	///////					String value = (String)stack.pop();
	///////					String varName = (String)stack.pop();
						
	///////					if (varName.equals("this")) {
	///////						buf.append(unpackager.castThis(className));
	///////					} else {
	///////						buf.append(varName);
	///////					}
						
						buf.append(".");
	///////					buf.append(fieldName);
						buf.append(" = ");
						
	///////					if (type.equals("boolean")) {
	///////						value = Unpackager.toBooleanValue(value);
	///////					}
	///////					buf.append(value);
						
						linesVector.addElement(buf.toString());
					}
						break;
					
					// istore, lstore, fstore, dstore, astore
					case 54:
					case 55:
					case 56:
					case 57:
					case 58:
					{
						StringBuffer buf = new StringBuffer();
						int index = -1; /* --&-&-&-&-- WAS Integer.parseInt(bytecode.getArg()); */
						boolean varTypeIsBoolean = false;
						
/**********************
						if (locals.length != 0) {
							LocalVariable localVar = locals[index];
							if (!declaredVars[index]) {
								if (localVar.isFinal()) {
									buf.append("final ");
								}
								buf.append(localVar.getVariableType()).append(' ');
								declaredVars[index] = true;
							}
							
							buf.append(localVar.getVariableName());
							if (localVar.getVariableType().equals("boolean")) {
								varTypeIsBoolean = true;
							}
						} else {
							throw new InternalError("method has no LocalVariableTable");
						}
**********************/
						
						buf.append(" = ");
						String value = (String)stack.pop();
						if (varTypeIsBoolean) {
							value = Unpackager.toBooleanValue(value);
						}
						
						buf.append(value);
						linesVector.addElement(buf.toString());
					}
						break;
					
					// iastore, lastore, fastore, dastore, aastore
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
						StringBuffer buf = new StringBuffer();
						
						String value = (String)stack.pop();
						String index = (String)stack.pop();
						String obj = (String)stack.pop();
						
//&&&&&&&&						if (obj.startsWith("new")) {
//&&&&&&&&							// Java 1.1 way to create an array:
//&&&&&&&&							//			return new int[][] { { 1, 2 }, { 3, 4 } };
//&&&&&&&&							//	(or)	return new int[][] {
//&&&&&&&&							//				new int[] { 1, 2 },
//&&&&&&&&							//				new int[] { 3, 4 }
//&&&&&&&&							//			};
//&&&&&&&&							
//&&&&&&&&							BytecodeItem first_newarray = null;
//&&&&&&&&							// scan backward to find the first '(a)newarray'
//&&&&&&&&							// before 'xastore'
//&&&&&&&&							for (int i = pc-1; i >= 0; i--) {
//&&&&&&&&								BytecodeItem bi = bytecodes[i];
//&&&&&&&&								if (bi != null) {
//&&&&&&&&									byte op = bi.getOpcode();
//&&&&&&&&									if ((op >= 79) && (op <= 86)) {
//&&&&&&&&										// 'xastore' found
//&&&&&&&&										break;
//&&&&&&&&									}
//&&&&&&&&									
//&&&&&&&&									if (op == -68) { // -68 is 188
//&&&&&&&&										// 'newarray' found
//&&&&&&&&										first_newarray = bi;
//&&&&&&&&									} 
//&&&&&&&&								}
//&&&&&&&&							}
//&&&&&&&&							
//&&&&&&&&							if (first_newarray == null) {
//&&&&&&&&								throw new Exception("bad constant array initializer"); //BadBytecodesException("bad constant array initializer");
//&&&&&&&&							}
//&&&&&&&&							
//&&&&&&&&							String arg = first_newarray.getArg();
//&&&&&&&&							int totalDimensionCount = 1 + StringUtilities.countSymbol(arg, '[');
//&&&&&&&&							arg = arg.substring(totalDimensionCount - 1);
//&&&&&&&&							String type = unpackager.unpackage(ClassUtilities.describeJVMType(arg));
//&&&&&&&&							int numberOfMainElements = Integer.parseInt(first_newarray.getArg2());
//&&&&&&&&							
//&&&&&&&&							int[] groupEnds = new int[numberOfMainElements];
//&&&&&&&&							int groupPos = 0;
//&&&&&&&&							
//&&&&&&&&							// scan forward to find 'xastore' bytecodes:
//&&&&&&&&							// 'numberOfMainElements' times
//&&&&&&&&							// by groups of 'totalDimensionCount' similar bytecodes
//&&&&&&&&							for (int i = pc+1; i <= endPC; i++) {
//&&&&&&&&								BytecodeItem bi = bytecodes[i];
//&&&&&&&&								if (bi != null) {
//&&&&&&&&									byte op = bi.getOpcode();
//&&&&&&&&									if ((op >= 79) && (op <= 86)) {
//&&&&&&&&										// 'xastore' found
//&&&&&&&&										int similarBytecodeCount = 0;
//&&&&&&&&										
//&&&&&&&&										for (int jj = i; jj <= endPC; ) {
//&&&&&&&&											byte op2 = bytecodes[jj].getOpcode();
//&&&&&&&&											if ((op2 >= 79) && (op2 <= 86)) {
//&&&&&&&&												similarBytecodeCount++;
//&&&&&&&&											} else {
//&&&&&&&&												break;
//&&&&&&&&											}
//&&&&&&&&											
//&&&&&&&&											if (similarBytecodeCount == totalDimensionCount) {
//&&&&&&&&												groupEnds[groupPos] = jj;
//&&&&&&&&												groupPos++;
//&&&&&&&&												i = jj + 1;
//&&&&&&&&												break;
//&&&&&&&&											}
//&&&&&&&&											
//&&&&&&&&											jj = BytecodeBlock.calculateNextPC(
//&&&&&&&&												bytecodes, jj, bytecodes.length - 1
//&&&&&&&&											);
//&&&&&&&&										}
//&&&&&&&&									}
//&&&&&&&&								}
//&&&&&&&&							}
//&&&&&&&&							
//&&&&&&&&							buf.append("new ");
//&&&&&&&&							buf.append(type);
//&&&&&&&&							for (int i = 0; i < totalDimensionCount; i++) {
//&&&&&&&&								buf.append("[]");
//&&&&&&&&							}
//&&&&&&&&							buf.append(" {");
//&&&&&&&&							
//&&&&&&&&							Vector values = new Vector();
//&&&&&&&&							int lastGroupEnd = groupEnds[numberOfMainElements-1];
//&&&&&&&&							
//&&&&&&&&							// collect all values
//&&&&&&&&							for (int i = first_newarray.getPCValue(); i <= lastGroupEnd; i++) {
//&&&&&&&&								BytecodeItem bi = bytecodes[i];
//&&&&&&&&								if (bi != null) {
//&&&&&&&&									byte op = bi.getOpcode();
//&&&&&&&&									if ((op >= 79) && (op <= 86)) {
//&&&&&&&&										int previous = BytecodeBlock.calculatePreviousPC(
//&&&&&&&&											bytecodes, i
//&&&&&&&&										);
//&&&&&&&&										SpecialStack previousStack = calculator.getStackAt(previous);
//&&&&&&&&										
//&&&&&&&&										String currentValue = (String)previousStack.pop();
//&&&&&&&&										String currentIndex = (String)previousStack.pop();
//&&&&&&&&										String currentObj = (String)previousStack.pop();
//&&&&&&&&										
//&&&&&&&&										int currentLevel = StringUtilities.countSymbol(
//&&&&&&&&											currentObj, '['
//&&&&&&&&										);
//&&&&&&&&										
//&&&&&&&&										if (currentLevel > 1) {
//&&&&&&&&											currentValue = StringUtilities.getAfterFirst(
//&&&&&&&&												currentValue, '['
//&&&&&&&&											);
//&&&&&&&&											currentValue = StringUtilities.getBefore(
//&&&&&&&&												currentValue, ']'
//&&&&&&&&											);
//&&&&&&&&										}
//&&&&&&&&										
//&&&&&&&&										// (level = 1) [level:index_in_array] value
//&&&&&&&&										// (level > 1) [level:index_in_array] number of values
//&&&&&&&&										values.addElement(
//&&&&&&&&											"[" + String.valueOf(currentLevel) + ":" +
//&&&&&&&&											currentIndex + "] " + currentValue
//&&&&&&&&										);
//&&&&&&&&									}
//&&&&&&&&								}
//&&&&&&&&							}
//&&&&&&&&							
//&&&&&&&&							// check & arrange values:
//&&&&&&&&							//		[1:0,1,...] value1_1, value1_2, ...
//&&&&&&&&							//		[2:0] n1
//&&&&&&&&							//		[1:0,1,...] value2_1, value2_2, ...
//&&&&&&&&							//		[2:1] n1
//&&&&&&&&							//		...
//&&&&&&&&							////////// ............ in progress
//&&&&&&&&							///////// ............ assume it's arranged (assume the compiler is good)
//&&&&&&&&							Vector arrangedValues = new Vector();
//&&&&&&&&							int valuesSize = values.size();
//&&&&&&&&							
//&&&&&&&&							for (int i = 0; i < valuesSize; i++) {
//&&&&&&&&								String currentValue = (String)values.elementAt(i);
//&&&&&&&&								String currentLevel = StringUtilities.getBefore(
//&&&&&&&&									currentValue.substring(1), ':'
//&&&&&&&&								);
//&&&&&&&&								currentValue = StringUtilities.getAfterFirst(currentValue, ' ');
//&&&&&&&&								
//&&&&&&&&								if (currentLevel.equals("1")) {
//&&&&&&&&									StringBuffer level1InfoBuffer = new StringBuffer();
//&&&&&&&&									level1InfoBuffer.append("[1:0");
//&&&&&&&&									int theIndex = 0;
//&&&&&&&&									
//&&&&&&&&									StringBuffer level1Buffer = new StringBuffer();
//&&&&&&&&									level1Buffer.append(currentValue);
//&&&&&&&&									while (((String)values.elementAt(i+1)).charAt(1) == '1') {
//&&&&&&&&										String nextValue = StringUtilities.getAfterFirst(
//&&&&&&&&											(String)values.elementAt(i+1), ' '
//&&&&&&&&										);
//&&&&&&&&										level1Buffer.append(", ").append(nextValue);
//&&&&&&&&										
//&&&&&&&&										i++;
//&&&&&&&&										theIndex++;
//&&&&&&&&										level1InfoBuffer.append(',').append(theIndex);
//&&&&&&&&									}
//&&&&&&&&									
//&&&&&&&&									arrangedValues.addElement(
//&&&&&&&&										level1InfoBuffer.toString() + "] "
//&&&&&&&&										+ level1Buffer.toString()
//&&&&&&&&									);
//&&&&&&&&								} else {
//&&&&&&&&									arrangedValues.addElement(values.elementAt(i));
//&&&&&&&&								}
//&&&&&&&&							}
//&&&&&&&&							values = arrangedValues;
//&&&&&&&&							
//&&&&&&&&							// arranged values -> java source style
//&&&&&&&&							
//&&&&&&&&							// [1:0,1] i1, i2			new int[][] {
//&&&&&&&&							// [2:0] 2			 ->			new int[] { i1, i2 },
//&&&&&&&&							// [1:0,1] i3, i4				new int[] { i3, i4 }
//&&&&&&&&							// [2:1] 2					};
//&&&&&&&&							
//&&&&&&&&							int spaceCount = this.oneSpaceString.length();
//&&&&&&&&							int previousLevel = totalDimensionCount;
//&&&&&&&&							valuesSize = values.size();
//&&&&&&&&							
//&&&&&&&&							for (int i = 0; i < valuesSize; i++) {
//&&&&&&&&								String currentValue = (String)values.elementAt(i);
//&&&&&&&&								int currentLevel = Integer.parseInt(
//&&&&&&&&									StringUtilities.getBefore(currentValue.substring(1), ':')
//&&&&&&&&								);
//&&&&&&&&								currentValue = StringUtilities.getAfterFirst(currentValue, ' ');
//&&&&&&&&								
//&&&&&&&&								
//&&&&&&&&								
//&&&&&&&&								if (currentLevel < previousLevel) {
//&&&&&&&&									int levelDelta = previousLevel - currentLevel;
//&&&&&&&&									for (int n = (levelDelta - 1); n >= 0; n--) {
//&&&&&&&&										buf.append('\n').append(spaceString);
//&&&&&&&&										int tabCount = totalDimensionCount - n - 1;
//&&&&&&&&										StringUtilities.addSpaces(buf, spaceCount*tabCount);
//&&&&&&&&										buf.append("new ").append(type);
//&&&&&&&&										for (int nn = 0; nn <= n; nn++) {
//&&&&&&&&											buf.append("[]");
//&&&&&&&&										}
//&&&&&&&&										buf.append(" {");
//&&&&&&&&									}
//&&&&&&&&									
//&&&&&&&&									if (currentLevel == 1) {
//&&&&&&&&										buf.append(' ').append(currentValue).append(' ');
//&&&&&&&&									}
//&&&&&&&&								} else if (currentLevel > previousLevel) {
//&&&&&&&&									if (previousLevel != 1) {
//&&&&&&&&										buf.append("\n").append(spaceString);
//&&&&&&&&										int tabCount = totalDimensionCount - currentLevel + 1;
//&&&&&&&&										StringUtilities.addSpaces(buf, spaceCount*tabCount);
//&&&&&&&&									}
//&&&&&&&&									
//&&&&&&&&									buf.append('}');
//&&&&&&&&									
//&&&&&&&&									if (i < (valuesSize - 1)) {
//&&&&&&&&										int nextLevel = Integer.parseInt(
//&&&&&&&&											StringUtilities.getBefore(
//&&&&&&&&												((String)values.elementAt(i+1)).substring(1),
//&&&&&&&&												':'
//&&&&&&&&											)
//&&&&&&&&										);
//&&&&&&&&										
//&&&&&&&&										if (nextLevel < currentLevel) {
//&&&&&&&&											buf.append(',');
//&&&&&&&&										}
//&&&&&&&&									}
//&&&&&&&&								} else { // if (currentLevel == previousLevel)
//&&&&&&&&									throw new InternalError("bad format");
//&&&&&&&&								}
//&&&&&&&&								
//&&&&&&&&								previousLevel = currentLevel;
//&&&&&&&&							}
//&&&&&&&&							
//&&&&&&&&							buf.append('\n').append(spaceString).append('}');
//&&&&&&&&							
//&&&&&&&&							// "extra push" the big array
//&&&&&&&&							extraPush = buf.toString();
//&&&&&&&&							extraPC = BytecodeBlock.calculatePreviousPC(
//&&&&&&&&								bytecodes, first_newarray.getPCValue()
//&&&&&&&&							);
//&&&&&&&&							
//&&&&&&&&							// change 'pc' value
//&&&&&&&&							pc = lastGroupEnd;
//&&&&&&&&						} else {
//&&&&&&&&							buf.append(obj).append('[').append(index).append(']');
//&&&&&&&&							buf.append(" = ").append(value);
//&&&&&&&&							linesVector.addElement(buf.toString());
//&&&&&&&&						}
					}
						break;
					
					// iinc
					case 132:
					{
						StringBuffer buf = new StringBuffer();
//////////						String index = bytecode.getArg();
						
//////////						if (locals.length != 0) {
//////////							buf.append(locals[Integer.parseInt(index)].getVariableName());
//////////						} else {
//////////							throw new InternalError("method has no LocalVariableTable");
//////////						}
						
//////////						String incValue = bytecode.getArg2();
//////////						if (incValue.equals("1")) {
//////////							buf.append("++");
//////////						} else if (incValue.equals("-1")) {
//////////							buf.append("--");
//////////						} else if (incValue.indexOf("-") == 0) {
//////////							buf.append(" -= " + incValue.substring(1));
//////////						} else {
//////////							buf.append(" += " + incValue);
//////////						}
						
						linesVector.addElement(buf.toString());
					}
						break;
					
					// ireturn, lreturn, freturn, dreturn, areturn
					case 172:
					case 173:
					case 174:
					case 175:
					case 176:
					{
						String value = (String)stack.pop();
						linesVector.addElement("return " + value);
					}
						break;
					
					// return
					case 177:
						linesVector.addElement("return");
						break;
					
					// xcmpx & branch bytecodes
					case 148: case 149: case 150: case 151: case 152:
					case 153: case 154: case 155: case 156: case 157: case 158:
					case 159: case 160: case 161: case 162: case 163: case 164:
					case 165: case 166:
					case 167: case 200:
					case 168: case 201: case 169: case 209:
					case 170: case 171:
					case 198: case 199:
						throw new InternalError(
							"BlockDecompiler: can't handle "
/////////////							+ bytecode.getPCValue() + " " + bytecode.getBytecode() +
/////////////							", method '" + this.method.getMethodName() /*method.getNameWithParameters*/ +
/////////////							"' (from " + startPC + " to " + endPC + ")"
						);
					
					// new/invoke bytecodes
					// ??? case 188: case 189: case 197:
					case 182: case 183: case 184: case 185: case 186: case 187:
					{
						Object stackTop = calculator.getStackAt(pc).pop();
						
						if (((String)stackTop).startsWith(" (void) ")) {
							linesVector.addElement(((String)stackTop).substring(8));
						} else {
							linesVector.addElement("(?)" + stackTop);
						}
					}
						break;
					
					default:
						break;
				}
				
				// check "extra push"
				if (extraPush.length() > 0) {
					if (extraPC != -1) {
						stack = calculator.getStackAt(extraPC);
						extraPC = -1;
					} else {
						// empty stack
						stack = new SpecialStack(calculator.getMaxStack());
					}
					
					stack.push(extraPush);
					extraPush = "";
				} else {
					stack = calculator.getStackAt(pc);
				}
////////////			}
		}
}catch(Exception e){
System.out.println("*** An exception caught while decompiling bytecode block ***");
e.printStackTrace(System.out);
System.out.println("************************************************************");
}
		
		// Cleaning up 'lines'
		this.lines = ArrayUtilities.vectorToStringArray(linesVector);
		if (lines != null) {
			int lineCount = lines.length;
			stack = calculator.getStackAt(endPC);
			int stackSize = stack.getCurrentSize();
			
			if (stackSize > 0) {
				String[] stackArray = new String[stackSize];
				for (int i = stackSize-1; i >= 0; i--) {
					stackArray[i] = (String)stack.pop();
				}
				
				int currPos = 0;
				for (int i = 0; i < stackSize; i++) {
					for (int j = currPos; j < lineCount; j++) {
						String currentLine = lines[j];
						if (currentLine != null) {
							if (currentLine.startsWith("(?)")) {
								int indexOf = currentLine.indexOf(stackArray[i]);
								if ((indexOf == 3) || (indexOf == 4)) {
									currPos = j;
									lines[j] = stackArray[i];
								} else {
									lines[j] = null;
								}
							} else if (currentLine.startsWith("(void) ")) {
								lines[j] = currentLine.substring(6);
							}
						}
					}
				}
			} else {
				for (int i = 0; i < lineCount; i++) {
					if (lines[i].startsWith("(?)")) {
						lines[i] = null;
					}
				}
			}
			
			// To hide some values from the stack,
			// StackCalculator pushes (' ' + value)
			for (int i = 0; i < lineCount; i++) {
				String currentLine = lines[i];
				if ((currentLine != null) && (currentLine.length() > 0)) {
					if (currentLine.charAt(0) == ' ') {
						lines[i] = currentLine.substring(1);
					}
				}
			}
		}
	}
	
	public String getString() {
		if ((lines != null) && (lines.length > 0)) {
			int len = lines.length;
			StringBuffer buf = new StringBuffer();
			
////////////			boolean methodIsSynthetic = this.method.isSynthetic();
			
			String current;
			for (int i = 0; i < len; i++) {
				current = lines[i];
				if (current != null) {
					buf.append(spaceString);
///////////					if (methodIsSynthetic) {
///////////						buf.append("/* ");
///////////					}
					buf.append(current);
					
///////////					if (methodIsSynthetic) {
///////////						buf.append("; */\n");
///////////					} else {
///////////						buf.append(";\n");
///////////					}
				}
			}
			
			return buf.toString();
		} else {
			return "";
		}
	}
	
	/**
	 *	@return		the information about this <code>BlockDecompiler</code>:
	 *				method's name, start and end 'pc' values.
	 *				(useful for debugging)
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(super.getClass().getName());
		buf.append(" [");
		
		buf.append("method '").append(this./*method.getNameWithParameters*/ method.getMethodName()).append("', ");
		buf.append("start pc = ").append(this.startPC).append(", ");
		buf.append("end pc = ").append(this.endPC);
		
		buf.append(']');
		return buf.toString();
	}
	
}
