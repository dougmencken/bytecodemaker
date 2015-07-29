/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.storage;

import java.io.*;

import douglas.mencken.io.ByteArrayIStream;
import douglas.mencken.io.ByteArrayOStream;
import douglas.mencken.tools.UsefulMessageDialogs;

/**
 *	<code>TryCatchFinallyBlock</code>
 *
 *	@version	0.87d2
 *	@since		Bytecode Maker 0.5.9
 */

public class TryCatchFinallyBlock extends JavaSourceBlock implements Externalizable {
	
	protected int[] handlerPCs;
	protected int catchRef;
	
	/**
	 *	(static method)
	 */
	public static TryCatchFinallyBlock combineTryCatchBlocks(TryCatchFinallyBlock[] blocks) {
		if (blocks == null) return null;
		
		int blockCount = blocks.length;
		if (blocks.length == 0) return null;
		if (blocks.length == 1) return blocks[0];
		
		JavaMethod method = blocks[0].getOwnerMethod();
		int codeLength = method.getCodeLength();
		
		// ...
		
		return blocks[0];
	}
	
	/**
	 *	Searches for try-catch block end.
	 *	
	 *	(static method)
	 */
	public static int findTryCatchBlockEnd(JVMInstructionSet instructions, int startPC) {
		// ...
		
		return -1;
	}
	
// FROM THE ***VERY*** OLD VERSIONS OF 'ClassDecompiler'

//	protected void treatCatches(int level) {
//		ExceptionItem[] catches = ...;
//		int clen = catches.length;
//		
//		if (clen > 1) {
//			for (int x = 0; x < clen; x++) {
//				// ...
//				
//				int end = codeLength;
//				Bytecode end_bytecode = rawBytecodes[curr_catch.end_pc];
//				if ((end_bytecode.getOpcode() == (byte)167) || (end_bytecode.getOpcode() == (byte)200) ||
//					 (end_bytecode.getOpcode() == (byte)168) || (end_bytecode.getOpcode() == (byte)201)) {
//					end = Integer.parseInt(end_bytecode.getArg()) + end_bytecode.getPCValue();
//					if (end < curr_catch.end_pc) end = method.getCodeLength();
//				}
//				
//				curr_catch.handler_end = calculatePreviousPC(end);
//			}
//			
//			int[] values = new int[clen];
//			int curr_val = 1;
//			int count = clen;
//			int pos = 0;
//			
//			ExceptionItem curr_catch, next_catch;
//			for (int x = 0; x < (clen - 1); x++) {
//				curr_catch = catches[x];
//				next_catch = catches[x+1];
//				
//				if ((curr_catch.start_pc == next_catch.start_pc) &&
//					 (curr_catch.end_pc == next_catch.end_pc)) {
//					curr_val++;
//					count--;
//				} else if (next_catch.catch_type.equals("finally")) {
//					curr_val++;
//					count--;
//				} else {
//					values[pos] = curr_val;
//					curr_val = 1;
//					pos++;
//				}
//			}
//			values[pos] = curr_val;
//			
//			TryItem[] tryItems = new TryItem[count];
//			pos = 0;
//			for (int x = 0; x < clen; x += values[pos], pos++) {
//				tryItems[pos] = new TryItem(catches[x].start_pc, catches[x].end_pc);
//				
//				for (int y = 0; y < values[pos]; y++) {
//					int catchEnd = catches[x+y].handler_end;
//					if ((y+1) != values[pos]) {
//						catchEnd = calculatePreviousPC(catches[x+y+1].handler_pc);
//					}
//					
//					tryItems[pos].addCatchStart(catches[x+y].handler_pc);
//					tryItems[pos].addCatchEnd(catchEnd);
//					tryItems[pos].addCatchType(catches[x+y].catch_type);
//				}
//			}
//			
//			for (int x = 0; x < count; x++) {
//				for (int y = 0; y < count; y++) {
//					if ( (x != y) &&
//						  (tryItems[x].getParent() != tryItems[y]) &&
//						  (tryItems[y].getParent() != tryItems[x]) ) {
//						tryItems[x].checkEnclose(tryItems[y]);
//					}
//				}
//			}
//			
//			int curr_pc = tryItems[0].tryStart;
//			if (curr_pc > 0) {
//				treatBytecodes(0, curr_pc, level);
//				buf.append("\n");
//			}
//			
//			for (int x = 0; x < count; x++) {
//				TryItem current = tryItems[x];
//				
//		// System.out.println(current);
//				
//				curr_pc = current.tryStart;
//				StringUtilities.addSpaces(buf, spaceCount*level);
//				
//				TryItem[] enclosed = current.getEnclosed();
//				TryItem[] c_enclosed = current.getEnclosedInCatches();
//				if ((enclosed == null) && (c_enclosed == null)) {
//					buf.append("try {\n");
//					treatBytecodes(curr_pc, current.tryEnd, level+1, true);
//					StringUtilities.addSpaces(buf, spaceCount*level);
//					buf.append("}");
//					curr_pc = current.tryEnd;
//					
//					int catchCount = current.getCatchCount();
//					int catchStart, catchEnd;
//					String catchType;
//					
//					for (int counter = 0; counter < catchCount; counter++) {
//						catchType = current.getCatchType(counter);
//						
//						if (catchType.equals("finally")) {
//							buf.append(" finally {");
//						} else {
//							buf.append(" catch (");
//							buf.append(unpackage(catchType));
//							buf.append(") {");
//						}
//						buf.append("\n");

//						catchStart = current.getCatchStart(counter);
//						catchEnd = current.getCatchEnd(counter);
//						treatBytecodes(catchStart, catchEnd, level+1, true);
//						
//						StringUtilities.addSpaces(buf, spaceCount*level);
//						buf.append("}");
//						curr_pc = calculateNextPC(catchEnd);
//					}
//					
//					buf.append("\n");
//				} else if (c_enclosed == null) {
//					treatCatchesWithEnclosed(current, level);
//					buf.append("\n");
//				} else if (enclosed == null) {
//					treatCatchesWithEnclosedInCatches(current, level);
//					buf.append("\n");
//				} else {
//					treatCatchesWithEnclosed(current, level);
//					treatCatchesWithEnclosedInCatches(current, level);
//					buf.append("\n");
//				}
//				
//				if ((x+1) != count) {
//					int nextStart = tryItems[x+1].tryStart;
//					if (curr_pc < nextStart) {
//						buf.append("\n");
//						treatBytecodes(curr_pc, nextStart, level);
//					}
//					buf.append("\n");
//					curr_pc = nextStart;
//				}
//			}
//			
//			if (curr_pc < lastPC) {
//				buf.append("\n");
//				treatBytecodes(curr_pc, lastPC, level);
//			}
//		}
//	}
	
	
	/** 
	 *	The constructor.
	 */
	public TryCatchFinallyBlock(JavaMethod owner, int start, int end, int handler, int catchref) {
		super(owner, 2, JavaSourceBlock.BLOCK_TYPE_try_catch);
		this.init(start, end, handler, catchref);
	}
	
	public TryCatchFinallyBlock() {
		this(null, 0, 0, 0, -1);
	}
	
	public void init(int startpc, int endpc, int handlerpc, int catchref) {
		super.startPC = startpc;
		super.endPC = endpc;
		this.handlerPCs = new int[] { handlerpc };
		this.catchRef = catchref;
	}
	
	public void writeExternal(ObjectOutput out) throws IOException {
    	out.writeShort(super.startPC);
		out.writeShort(super.endPC);
		out.writeShort(this.handlerPCs[0]);
		out.writeShort(this.catchRef);
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int startpc = in.readUnsignedShort();
		int endpc = in.readUnsignedShort();
		int handlerpc = in.readUnsignedShort();
		int catchref = in.readUnsignedShort();
		
		this.init(startpc, endpc, handlerpc, catchref);
	}
	
	public int getHandlerPCValue() {
		return this.handlerPCs[0];
	}
	
	public int getCatchTypeReference() {
		return this.catchRef;
	}
	
	public int[] getPCValues() {
		return new int[] { super.startPC, super.endPC, this.handlerPCs[0] };
	}
	
	public String getCatchType() {
		if (this.catchRef == 0) return "finally";
		
		if (this.ownerMethod == null) return "* no owner *";
		JavaConstantPool pool = this.ownerMethod.getOwnerClass().getConstantPool();
		if (pool == null) return "* no pool manager *";
		
		return pool.getConstantContents(this.catchRef);
	}
	
	public boolean equals(Object obj) {
		if ((obj != null) && (obj instanceof TryCatchFinallyBlock)) {
			TryCatchFinallyBlock block = (TryCatchFinallyBlock)obj;
			
			if (block.startPC == super.startPC) {
				if (block.endPC == super.endPC) {
					if (block.catchRef == this.catchRef) {
						int count1 = block.handlerPCs.length;
						int count2 = this.handlerPCs.length;
						if (count1 == count2) {
							for (int i = 0; i < count1; i++) {
								if (block.handlerPCs[i] != this.handlerPCs[i]) {
									return false;
								}
							}
							
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer(this.getClass().getName());
		buf.append(" [startPC = ").append(super.getStartPCValue());
		buf.append(", endPC = ").append(super.getEndPCValue());
		buf.append(", handlerPC = ").append(this.getHandlerPCValue());
		buf.append(", catchRef = ").append(this.getCatchTypeReference()).append(']');
		
		return buf.toString();
	}
	
	/**
	 *	Block type cannot be changed.
	 */
	public void setBlockType(int newType) {
		throw new NoSuchMethodError(super.getClass().getName() + ".setBlockType(int)");
	}
	
}