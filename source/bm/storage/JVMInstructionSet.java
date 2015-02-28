// ===========================================================================
// JVMInstructionSet.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.io.*;

import douglas.mencken.util.ByteTransformer;

/**
 *	<code>JVMInstructionSet</code> is a storage for a single
 *	set of instructions.<br>
 *	
 *	@version	1.40d1
 *	@since		Bytecode Maker 0.5.8
 */

public class JVMInstructionSet extends Object
implements JavaMethodMember, Externalizable {
	
	public static final String[] MNEMONICS = {
		"nop", "aconst_null", "iconst_m1", "iconst_0",
		"iconst_1", "iconst_2", "iconst_3", "iconst_4",
		"iconst_5", "lconst_0", "lconst_1", "fconst_0",
		"fconst_1", "fconst_2", "dconst_0", "dconst_1",
		"bipush", "sipush", "ldc1", "ldc2", "ldc2w",
		"iload", "lload", "fload", "dload", "aload",
		"iload_0", "iload_1", "iload_2", "iload_3",
		"lload_0", "lload_1", "lload_2", "lload_3",
		"fload_0", "fload_1", "fload_2", "fload_3",
		"dload_0", "dload_1", "dload_2", "dload_3",
		"aload_0", "aload_1", "aload_2", "aload_3",
		"iaload", "laload", "faload", "daload",
		"aaload", "baload", "caload", "saload",
		"istore", "lstore", "fstore", "dstore", "astore",
		"istore_0", "istore_1", "istore_2", "istore_3",
		"lstore_0", "lstore_1", "lstore_2", "lstore_3",
		"fstore_0", "fstore_1", "fstore_2", "fstore_3",
		"dstore_0", "dstore_1", "dstore_2", "dstore_3",
		"astore_0", "astore_1", "astore_2", "astore_3",
		"iastore", "lastore", "fastore", "dastore",
		"aastore", "bastore", "castore", "sastore",
		"pop", "pop2", "dup", "dup_x1", "dup_x2",
		"dup2", "dup2_x1", "dup2_x2", "swap",
		"iadd", "ladd", "fadd", "dadd",
		"isub", "lsub", "fsub", "dsub",
		"imul", "lmul", "fmul", "dmul",
		"idiv", "ldiv", "fdiv", "ddiv",
		"irem", "lrem", "frem", "drem",
		"ineg", "lneg", "fneg", "dneg",
		"ishl", "lshl", "ishr", "lshr", "iushr", "lushr",
		"iand", "land", "ior", "lor", "ixor", "lxor",
		"iinc", "i2l", "i2f", "i2d", "l2i", "l2f", "l2d",
		"f2i", "f2l", "f2d", "d2i", "d2l", "d2f",
		"int2byte", "int2char", "int2short",
		"lcmp", "fcmpl", "fcmpq", "dcmpl", "dcmpq",
		"ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle",
		"if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge",
		"if_icmpgt", "if_icmple", "if_acmpeq", "if_acmpne",
		"goto", "jsr", "ret", "tableswitch", "lookupswitch",
		"ireturn", "lreturn", "freturn", "dreturn", "areturn",
		"return", "getstatic", "putstatic", "getfield",
		"putfield", "invokevirtual", "invokenonvirtual",
		"invokestatic", "invokeinterface", "newfromname",
		"new", "newarray", "anewarray", "arraylength",
		"athrow", "checkcast", "instanceof", "monitorenter",
		"monitorexit", "* wide *", "multianewarray",
		"ifnull", "ifnonull", "goto_2", "jsr_w", "breakpoint",
		"* unknown *", "* unknown *", "* unknown *",
		"* unknown *", "* unknown *", "* unknown *",
		"ret_w"
	};
	
	/**
	 *	Local variable load, store, and increment opcodes
	 *	can use a 16-bit index.
	 */
	public static final int[] WIDE_OPCODES = {
		21, 22, 23, 24, 25,
		54, 55, 56, 57, 58,
		132
	};
	
	protected JavaMethod ownerMethod = null;
	
	public JavaMethod getOwnerMethod() {
		return this.ownerMethod;
	}
	
	public void setOwnerMethod(JavaMethod owner) {
		// ...
		this.ownerMethod = owner;
	}
	
	/**
	 *	Writes an instruction set to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		// ...
	}
	
	/**
	 *	Reads an instruction set from the ObjectInput.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// ...
	}
	
	// ...
	
	
	/**
	 *	<code>CodeTransformer</code> transforms method's code
	 *	into instructions.
	 *
	 *	@version 1.15f6
	 */
	
	static class CodeTransformer extends Object {
		
		private CodeTransformer() { super(); }
		
		static JVMInstruction[] transformCode(byte[] code, JavaConstantPool pool) {
			if (code == null) {
				throw new IllegalArgumentException("'null' code");
			}
			if (pool == null) {
				throw new IllegalArgumentException("'null' constant pool");
			}
			
			int codelength = code.length;
			JVMInstruction[] instructions = new JVMInstruction[codelength];
			
			int value;
			boolean wide;
			
			for (int pc = 0; pc < codelength; pc++) {
				int opcode = ByteTransformer.toUnsignedByte(code[pc]);
				
				wide = false;
				if (opcode == /* wide */ 196) {
					wide = true;
					pc++;
					opcode = ByteTransformer.toUnsignedByte(code[pc]);
				}
				
				instructions[pc] = new JVMInstruction((byte)opcode);
				instructions[pc].setPCValue(pc);
				
				switch (opcode) {
					case 16:
					case 169:
						instructions[pc].setArgument1(String.valueOf(
							ByteTransformer.toUnsignedByte(code[pc+1])
						));
						pc++;
						break;
					case 17:
					case 209:
						instructions[pc].setArgument1(String.valueOf(
							ByteTransformer.toUnsignedShort(code, pc+1)
						));
						pc += 2;
						break;
					case 18:
						value = ByteTransformer.toUnsignedByte(code[pc+1]);
						instructions[pc].setArgument1("#" + value);
						instructions[pc].setArgument2(pool.getConstantContents(value));
						
						pc++;
						break;
					case 19:
					case 20:
						value = ByteTransformer.toUnsignedShort(code, pc+1);
						instructions[pc].setArgument1("#" + value);
						instructions[pc].setArgument2(pool.getConstantContents(value));
						
						pc += 2;
						break;
					
					// xload
					// xstore
					case 21: case 22: case 23: case 24: case 25:
					case 54: case 55: case 56: case 57: case 58:
						if (wide) {
							instructions[pc].setWide(true);
							instructions[pc].setArgument1(String.valueOf(
								ByteTransformer.toUnsignedShort(code, pc+1)
							));
							pc += 2;
						} else {
							instructions[pc].setArgument1(String.valueOf(
								ByteTransformer.toUnsignedByte(code[pc+1])
							));
							pc++;
						}
						break;
					
					case 132:
						if (wide) {
							instructions[pc].setWide(true);
							instructions[pc].setArgument1(String.valueOf(
								ByteTransformer.toUnsignedShort(code, pc+1)
							));
							instructions[pc].setArgument2(String.valueOf(
								/* signed short */ ByteTransformer.toSignedShort(code, pc+3)
							));
							pc += 4;
						} else {
							instructions[pc].setArgument1(String.valueOf(
								ByteTransformer.toUnsignedByte(code[pc+1])
							));
							instructions[pc].setArgument2("" + /* signed byte */ code[pc+2]);
							pc += 2;
						}
						break;
					
					case 153:
					case 154:
					case 155:
					case 156:
					case 157:
					case 158:
					
					case 159:
					case 160:
					case 161:
					case 162:
					case 163:
					case 164:
					case 165:
					case 166:
					
					case 167:
					case 168:
					
					case 198:
					case 199:
						value = /* signed short */ ByteTransformer.toSignedShort(code, pc+1);
						instructions[pc].setArgument1(Integer.toString(value));
						instructions[pc].setArgument2("(" + Integer.toString(value + pc) + ")");
						pc += 2;
						break;
					
					case 170:
						{
							int first_pc = pc;
							pc++;
							while (pc % 4 != 0) {
								pc++;
							}
							
							int def = ByteTransformer.toInteger(code, pc);
							int low = ByteTransformer.toInteger(code, pc+4);
							int high = ByteTransformer.toInteger(code, pc+8);
							pc += 12;
							
							instructions[first_pc].setArgument1(Integer.toString(def));
							instructions[first_pc].setArgument2(Integer.toString(low));
							instructions[first_pc].setArgument3(Integer.toString(high));
							
							int count = (high - low) + 1;
							String[] args = new String[count];
							 
							for (int j = 0; j < count; j++) {
								args[j] = Integer.toString(ByteTransformer.toInteger(code, pc));
								pc += 4;
							}
							
							instructions[first_pc].setAdditionalArguments(args);
							pc--;
						}
						break;
					case 171:
						{
							int first_pc = pc;
							pc++;
							while (pc % 4 != 0) {
								pc++;
							}
							
							int def = ByteTransformer.toInteger(code, pc);
							int count = ByteTransformer.toInteger(code, pc+4);
							pc += 8;
							
							instructions[first_pc].setArgument1(Integer.toString(def));
							instructions[first_pc].setArgument2(Integer.toString(count));
							
							count *= 2;
							String[] args = new String[count];
							
							for (int j = 0; j < count; j++) {
								args[j] = Integer.toString(ByteTransformer.toInteger(code, pc));
								pc += 4;
							}
							
							instructions[first_pc].setAdditionalArguments(args);
							pc--;
						}
						break;
					
					case 188:
						String type;
						value = code[pc+1];
						switch (value) {
							case  4:	type = "boolean"; break;
							case  5:	type = "char"; break;
							case  6:	type = "float"; break;
							case  7:	type = "double"; break;
							case  8:	type = "byte"; break;
							case  9:	type = "short"; break;
							case 10:	type = "int"; break;
							case 11:	type = "long"; break;
							default:	type = "* unknown *"; break;
						}
						
						instructions[pc].setArgument1(Integer.toString(value));
						instructions[pc].setArgument2(type);
						pc++;
						break;
					
					case 178:
					case 179:
					case 180:
					case 181:
					
					case 182:
					case 183:
					case 184:
					
					case 187:
					case 189:
					case 192:
					case 193:
						value = ByteTransformer.toUnsignedShort(code, pc+1);
						instructions[pc].setArgument1("#" + value);
						instructions[pc].setArgument2(
							'<' + pool.getConstantContents(value) + '>'
						);
						
						pc += 2;
						break;
					
					case 185:
						value = ByteTransformer.toUnsignedShort(code, pc+1);
						instructions[pc].setArgument1("#" + value);
						instructions[pc].setArgument2(
							'<' + pool.getConstantContents(value) + '>'
						);
						instructions[pc].setArgument3(Integer.toString(
							ByteTransformer.toUnsignedByte(code[pc+3])
						));
						
						pc += 4;
						break;
					
					case 197:
						value = ByteTransformer.toUnsignedShort(code, pc+1);
						instructions[pc].setArgument1("#" + value);
						instructions[pc].setArgument2(
							'<' + pool.getConstantContents(value) + '>'
						);
						instructions[pc].setArgument3(Integer.toString(
							ByteTransformer.toUnsignedByte(code[pc+3])
						));
						
						pc += 3;
						break;
					
					case 200:
					case 201:
						value =	ByteTransformer.toInteger(code, pc+1);
						instructions[pc].setArgument1(Integer.toString(value));
						instructions[pc].setArgument2("(" + Integer.toString(value + pc) + ")");
						
						pc += 4;
						break;
					
					default:
						// do nothing
						break;
				}
			}
			
			return instructions;
		}
		
	}
	
	/**
	 *	(static method)
	 */
	public static String opcodeToMnemonic(byte opcode) {
		return opcodeToMnemonic(opcode, false);
	}
	
	/**
	 *	(static method)
	 */
	public static String opcodeToMnemonic(byte opcode, boolean isWide) {
		int op = ByteTransformer.toUnsignedByte(opcode);
		
		StringBuffer buf = new StringBuffer();
		if ((op >=0) && (op < JVMInstructionSet.MNEMONICS.length)) {
			buf.append(JVMInstructionSet.MNEMONICS[op]);
			if (isWide) {
				buf.append("_w");
			}
		} else {
			buf.append("* unknown (").append(opcode).append(')');
			if (isWide) {
				buf.append(", wide");
			}
			buf.append(" *");
		}
		
		return buf.toString();
	}
	
}
