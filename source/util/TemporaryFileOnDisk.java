// ===========================================================================
//	TemporaryFileOnDisk.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.io.*;
import douglas.mencken.io.*;

import com.apple.mrj.MRJOSType;
import com.apple.mrj.MRJFileUtils;

/**
 *	<code>TemporaryFileOnDisk</code>
 *
 *	Note:	before each read/write operation, the input/output streams
 *			are opened, and then closed.
 *
 *	@version	1.33f2
 */

public class TemporaryFileOnDisk extends Object {
	
	private ByteArrayIStream in;
	private ByteArrayOStream out;
	protected RandomAccessFile file;
	
	private long toSkip = 0L;
	
	protected String folder;
	protected String fileName;
	protected String type;
	protected String creator;
	
	/**
	 *	Default constructor.
	 *	The default folder is the working directory.
	 *	The default name is "Temporary File".
	 */
	public TemporaryFileOnDisk() {
		this(System.getProperty("user.dir"), "Temporary File");
	}
	
	public TemporaryFileOnDisk(String name) {
		this(System.getProperty("user.dir"), name);
	}
	
	public TemporaryFileOnDisk(String folder, String name) {
		this.folder = folder;
		this.fileName = name;
		
		this.type = "BINA";
		this.creator = "????";
	}
	
	public String getFolder() {
		return this.folder;
	}
	
	public long getFileLength() {
		return (new File(this.folder, this.fileName)).length();
	}
	
	/**
	 *	Reads up to <code>count</code> bytes of data from the file into an 
	 *	array of bytes. This method blocks until at least one byte of input 
	 *	is available.
	 *
	 *	@return		the total number of bytes read into the buffer, or
	 *				<code>-1</code> if there is no more data because the end of
	 *				the file has been reached.
	 */
	public synchronized int read(byte[] array, int offset, int count) throws IOException {
		this.openRead();
		
		int nbytes = in.read(array, offset, count);
		this.close();
		
		return nbytes;
	}
	
	public int read(byte[] array) throws IOException {
		return this.read(array, 0, array.length);
    }
    
	/**
	 *	Reads exactly all available bytes from the file.
	 */
	public synchronized byte[] read() throws IOException {
		this.openRead();
		int size = (int)this.in.available();
		byte[] file = new byte[size];
		in.readFully(file, 0, size);
		this.close();
		
		return file;
    }
    
	/**
	 *	Read an object from the file.
	 */
	public synchronized Object readObject()
	throws IOException, ClassNotFoundException {
		this.openRead();
		Object obj = in.readObject();
		this.close();
		
		return obj;
	}
	
	/**
	 *	The next read/write occurs at nbytes position.
	 *
	 *	@param      nbytes   the number of bytes to be skipped.
	 *	@return     the actual number of bytes skipped.
	 */
	public long skip(long nbytes) throws IOException {
		if (nbytes > 0L) {
			this.toSkip = nbytes;
			return nbytes;
		}
		
		return 0L;
	}
	
	/**
	 *	Writes <code>count</code> bytes from the specified
	 *	byte array starting at <code>offset</code> to this file.
	 */
	public synchronized void write(byte[] array, int offset, int count) throws IOException {
		this.openWrite();
		out.write(array, offset, count);
		this.close();
	}
	
	public synchronized void write(byte[] array) throws IOException {
		this.openWrite();
		out.write(array, 0, array.length);
		this.close();
	}
	
	/**
	 *	Write the specified object to the file.
	 */
	public synchronized void writeObject(Object obj) throws IOException {
		this.openWrite();
		out.writeObject(obj);
		this.close();
	}
	
	protected void openRead() throws IOException {
		this.file = new RandomAccessFile(new File(this.folder, this.fileName), "r");
		byte[] data = new byte[(int)this.file.length()];
		this.file.readFully(data);
		
		this.in = new ByteArrayIStream(data);
		
		if (this.toSkip > 0L) {
			this.file.seek(this.toSkip);
			this.in.skip(this.toSkip);
			
			// skip only once
			this.toSkip = 0L;
		}
	}
	
	protected void openWrite() throws IOException {
		File folder = new File(this.folder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		File temporaryFile = new File(this.folder, this.fileName);
		this.file = new RandomAccessFile(temporaryFile, "rw");
		this.out = new ByteArrayOStream();
		
		MRJFileUtils.setFileType(temporaryFile, new MRJOSType(this.type));
		MRJFileUtils.setFileCreator(temporaryFile, new MRJOSType(this.creator));
	}
	
	public void flush() throws IOException {
		if (this.out != null) {
			this.out.flush();
			
			if ((new File(this.folder, this.fileName)).exists()) {
				this.file.seek(this.file.length());
			}
			this.file.write(this.out.toByteArray());
		}
	}
	
	protected void close() {
		try {
			this.flush();
			
			if (this.file != null) {
				this.file.close();
			}
		} catch (IOException ioe) {}
		
		this.in = null;
		this.out = null;
		this.file = null;
	}
	
	/**
	 *	Resets the file.
	 */
	public void reset() {
		try {
			if (this.file != null) {
				this.file.close();
			}
			
			this.in = null;
			this.out = null;
			this.file = null;
			
			FileOutputStream fos = new FileOutputStream(
				new File(this.folder, this.fileName)
			);
			fos.close();
		} catch (IOException ioe) {}
		
		this.toSkip = 0L;
	}
	
	public void setFileTypeAndCreator(String type, String creator) {
		if (type.length() != 4) throw new IllegalArgumentException();
		if (creator.length() != 4) throw new IllegalArgumentException();
		
		this.type = type;
		this.creator = creator;
	}
	
	/**
	 *	ret[0] - type
	 *	ret[1] - creator
	 */
	public String[] getFileTypeAndCreator() {
		return new String[] { this.type, this.creator };
	}
	
}