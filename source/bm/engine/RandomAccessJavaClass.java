/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.engine;

import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;
import java.lang.reflect.Modifier;

import douglas.mencken.bm.storage.JavaClass;

/**
 *	<code>RandomAccessJavaClass</code>
 *	
 *	@version	1.03f2
 *	@since		Bytecode Maker 0.5.9
 */

public class RandomAccessJavaClass extends Object {
	
	protected ObjectInput in;
	protected ObjectOutput out;
	
	public RandomAccessJavaClass(ObjectInput in) {
		this(in, null);
	}
	
	public RandomAccessJavaClass(ObjectOutput out) {
		this(null, out);
	}
	
	public RandomAccessJavaClass(ObjectInput in, ObjectOutput out) {
		this.in = in;
		this.out = out;
	}
	
	public void writeJavaClass(JavaClass clazz) throws IOException {
		if (this.out == null) {
			throw new NullPointerException("ObjectOutput is not specified");
		}
		
		clazz.writeExternal(this.out);
	}
	
	public JavaClass readJavaClass() throws IOException {
		if (this.in == null) {
			throw new NullPointerException("ObjectInput is not specified");
		}
		
		JavaClass clazz = new JavaClass(Modifier.PUBLIC | Modifier.SYNCHRONIZED, 0, 0);
		try {
			clazz.readExternal(this.in);
		} catch (ClassNotFoundException ignored) {}
		
		return clazz;
	}
	
}