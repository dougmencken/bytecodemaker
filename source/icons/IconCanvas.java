// ===========================================================================
//	IconCanvas.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.awt.*;
import java.io.*;

/**
 *	<code>IconCanvas</code>
 *
 *	@version	1.10
 */

public class IconCanvas extends Canvas implements Externalizable {
	
	private transient Icon theIcon;
	protected static int externalVersion = 0x00000120;	// 1.2.0
	
	public IconCanvas() {
		this(null);
	}
	
	public IconCanvas(Icon icon) {
		this.theIcon = icon;
		updateSize();
	}
	
	public Dimension getMinimumSize() {
		if (theIcon == null) {
			return new Dimension(16, 16);
		} else {
			return new Dimension(
				theIcon.getImage().getWidth(this) + 10,
				theIcon.getImage().getHeight(this) + 10
			);
		}
	}
	
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
	
	public void paint(Graphics g) {
		Dimension size = super.getSize();
		
		if ((theIcon != null) && (theIcon.getImage() != null)) {
			int xPos = (size.width - theIcon.getImage().getWidth(this))/2;
			int yPos = (size.height - theIcon.getImage().getHeight(this))/2;
			g.drawImage(theIcon.getImage(), xPos, yPos, this);
		} else {
			g.setColor(Color.white);
			g.fillRect(0, 0, size.width-1, size.height-1);
			g.setColor(Color.black);
			g.drawRect(0, 0, size.width-1, size.height-1);
		}
	}
	
	public void setIcon(Icon icon) {
		if (icon == this.theIcon) return;
		
		if (icon != null) {
			this.theIcon = new Icon(icon);
		} else {
			this.theIcon = null;
		}
		
		updateSize();
		repaint();
	}
	
	public Icon getIcon() {
		return new Icon(theIcon);
	}
	
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(externalVersion);
		
		out.writeUTF((theIcon == null) ? "none" : theIcon.getName());
		Rectangle r = super.getBounds();
		out.writeInt(r.x);
		out.writeInt(r.y);
		out.writeInt(r.width);
		out.writeInt(r.height);
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int version = in.readInt();
		if (version != externalVersion) {
			throw new IOException("readExternal: version mismatch for IconCanvas");
		}
		
		String className = in.readUTF();
		if (className.equals("none")) {
			this.theIcon = null;
		} else {
			try {
				Class iconClass = Class.forName("douglas.mencken.icons." + className);
				this.theIcon = (Icon)(iconClass.newInstance());
			} catch (Exception e) {
				this.theIcon = null;
			}
		}
		
		int x = in.readInt();
		int y = in.readInt();
		int width = in.readInt();
		int height = in.readInt();
		super.setBounds(x, y, width, height);
		
		repaint();
	}
	
	private void updateSize() {
		if (theIcon == null) {
			super.setSize(16, 16);
		} else {
			super.setSize(
				theIcon.getImage().getWidth(this) + 10,
				theIcon.getImage().getHeight(this) + 10
			);
		}
	}
	
}