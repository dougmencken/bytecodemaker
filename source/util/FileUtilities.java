// ===========================================================================
//	FileUtilities.java (part of douglas.mencken.util package)
// ===========================================================================

package douglas.mencken.util;

import java.io.*;
import java.util.Vector;

/**
 *	<code>FileUtilities</code>
 *
 *	@version 1.28f1
 */

public final class FileUtilities extends Object {
	
	public static final int MAGIC_NUMBER_JAVA_CLASS = /* -889275714 */ 0xCAFEBABE;
	public static final int	MAGIC_NUMBER_ZIP_ARCHIVE = 1347093252;
	
	/**
	 *	Don't let anyone instantiate this class.
	 */
	private FileUtilities() { super(); }
	
	
	public static boolean isClassFile(byte[] data) {
		int magicnumber = ByteTransformer.toInteger(data, 0);
		return (magicnumber == MAGIC_NUMBER_JAVA_CLASS);
	}
	
	public static boolean isZipArchive(byte[] data) {
		int magicnumber = ByteTransformer.toInteger(data, 0);
		return (magicnumber == MAGIC_NUMBER_ZIP_ARCHIVE);
	}
	
	public static boolean isClassFile(File folder, String name) {
		try {
			return (readMagicNumber(folder, name) == MAGIC_NUMBER_JAVA_CLASS);
		} catch (IOException exc) {
			return false;
		}
	}
	
	public static boolean isZipArchive(File folder, String name) {
		try {
			return (readMagicNumber(folder, name) == MAGIC_NUMBER_ZIP_ARCHIVE);
		} catch (IOException exc) {
			return false;
		}
	}
	
	/**
	 *	@return		true on success.
	 */
	public static boolean writeBytesToFile(byte[] data, String path) {
		if (path != null) {
			return writeBytesToFile(data, new File(path));
		}
		
		return false;
	}
	
	/**
	 *	@return		true on success.
	 */
	public static boolean writeBytesToFile(byte[] data, File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
			
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}
	
	/**
	 *	Note:	only one-byte symbols are supported.
	 */
	public static byte[] charsToBytes(char[] in) {
		int size = in.length;
		byte[] out = new byte[size];
		
		for (int i = 0; i < size; i++) {
			out[i] = (in[i] < 256) ? (byte)in[i] : 63 /* ? */;
		}
		
		return out;
	}
	
	/**
	 *	Searches for a file.
	 *	Returns 'null' if file not found.
	 */
	public static File findFile(File where, String name) {
		File[] allFiles = FileUtilities.getAllFiles(where);
		int count = allFiles.length;
		
		for (int i = 0; i < count; i++) {
			if (name.equals(allFiles[i].getName())) {
				return allFiles[i];
			}
		}
		
		return null;
	}
	
	/**
	 *	Copies a file. Returns <code>true</code> on success.
	 */
	public static boolean copyFile(File original, File copy) {
		try {
			FileInputStream fis = new FileInputStream(original);
			FileOutputStream fos = new FileOutputStream(copy);
			
			int len = fis.available();
			byte[] data = new byte[len];
			fis.read(data);
			fos.write(data);
			
			fis.close();
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			return false;
		}
		
		return true;
	}
	
	/**
	 *	Clears and removes the folder specified by parameter.
	 */
	public static boolean clearAndRemoveFolder(File folder) {
		if (folder == null) {
			throw new IllegalArgumentException();
		}
		
		if (folder.exists()) {
			String[] allFiles = folder.list();
			int count = allFiles.length;
			
			for (int i = 0; i < count; i++) {
				File current = new File(folder.getPath(), allFiles[i]);
				if (current.isFile()) {
					boolean success = current.delete();
					
					if (!success) {
						// can't delete (file is busy), just clear it
						try {
							FileOutputStream fos = new FileOutputStream(current.getPath(), false);
							fos.close();
						} catch (IOException ioe) {}
					}
				} else {
					clearAndRemoveFolder(current);
				}
			}
			
			return folder.delete();
		} else {
			return true;
		}
	}
	
	public static String[] checkAndFilterFileList(String[] in) {
		return checkAndFilterFileList(in, -1);
	}
	
	public static String[] checkAndFilterFileList(String[] in, int maxfiles) {
		if (in == null) {
			throw new IllegalArgumentException("can't check and filter 'null'");
		} else {
			int count = in.length;
			Vector validFiles = new Vector(count);
			
			int first = 0;
			if ((maxfiles > 0) && (count > maxfiles)) {
				first = count - maxfiles;
			}
			
			for (int i = first; i < count; i++) {
				File current = new File(in[i + first]);
				if (current.canRead()) {
					validFiles.addElement(current.getPath());
				}
			}
			
			return ArrayUtilities.vectorToStringArray(validFiles);
		}
	}
	
	public static int readMagicNumber(File dir, String name) throws IOException {
		if ((dir == null) || (name == null)) {
			throw new IllegalArgumentException("can't read magic number for 'null'");
		}
		
		File file = new File(dir, name);
		if (!file.canRead()) {
			throw new IllegalArgumentException("file is not readable");
		}
		
		int magicnumber = 0;
		if (file.length() >= 4) {
			FileInputStream fis = new FileInputStream(file);
			byte[] h = new byte[4];
			fis.read(h);
			fis.close();
			
			magicnumber = ByteTransformer.toInteger(h, 0);
		} else {
			throw new IllegalArgumentException("file is too short (length < 4)");
		}
		
		return magicnumber;
	}
	
	public static File[] getAllFiles(File dir) {
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException();
		}
		
		Vector allFiles = new Vector();
		extractFiles(dir, allFiles);
		
		int size = allFiles.size();
		File[] ret = new File[size];
		for (int i = 0; i < size; i++) {
			ret[i] = (File)allFiles.elementAt(i);
		}
		
		return ret;
	}
	
	private static void extractFiles(File dir, Vector allFiles) {
		File[] files = getFiles(dir);
		File[] folders = getFolders(dir);
		int filesCount = (files == null) ? 0 : files.length;
		int foldersCount = (folders == null) ? 0 : folders.length;
		
		if (filesCount != 0) {
			for (int i = 0; i < filesCount; i++) {
				allFiles.addElement(files[i]);
			}
		}
		if (foldersCount != 0) {
			for (int i = 0; i < foldersCount; i++) {
				extractFiles(folders[i], allFiles);
			}
		}
	}
	
	public static File[] getFiles(File dir) {
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException();
		}
		
		Vector files = new Vector();
		String[] list = dir.list();
		if ((list != null) && (list.length != 0)) {
			int len = list.length;
			for (int i = 0; i < len; i++) {
				File current = new File(dir, list[i]);
				if (current.canRead() && current.isFile()) {
					files.addElement(current);
				}
			}
			
			int size = files.size();
			File[] ret = new File[size];
			for (int i = 0; i < size; i++) {
				ret[i] = (File)files.elementAt(i);
			}
			
			return ret;
		}
		
		return null;
	}
	
	public static File[] getFolders(File dir) {
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException();
		}
		
		Vector files = new Vector();
		String[] list = dir.list();
		if ((list != null) && (list.length != 0)) {
			int len = list.length;
			for (int i = 0; i < len; i++) {
				File current = new File(dir, list[i]);
				if (current.canRead() && current.isDirectory()) {
					files.addElement(current);
				}
			}
			
			int size = files.size();
			File[] ret = new File[size];
			for (int i = 0; i < size; i++) {
				ret[i] = (File)files.elementAt(i);
			}
			
			return ret;
		}
		
		return null;
	}
	
	public static byte[] getByteArray(InputStream is) throws IOException {
		int len = 0;
		byte[] buff = new byte[512];
		for (;;) {
			if (len == buff.length) {
				// Allocate a new, bigger, buffer.
				byte newBuff[] = new byte[buff.length*2];
				System.arraycopy(buff, 0, newBuff, 0, len);
				buff = newBuff;
			}
			int r = is.read(buff, len, buff.length-len);
			
			if (r < 0) break;
			len += r;
		}
		
		is.close();
		return buff;
	}
	
	public static byte[] getByteArray(String fileName) throws IOException {
		return getByteArray(new FileInputStream(fileName));
	}
	
	public static byte[] getByteArray(File file) throws IOException {
		return getByteArray(new FileInputStream(file));
	}
	
}