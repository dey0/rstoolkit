package rstoolkit.client;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;

@SuppressWarnings("serial")
public class PaintDialog extends JDialog {

	private BufferedImage background;
	private Graphics2D g;

	public PaintDialog(Frame frame, Applet game) {
		super(frame);
		Dimension size = game.getSize();
		setSize(size);
        setUndecorated(true);
		setLocation(game.getLocationOnScreen());
		setBackground(new Color(0, 0, 0, 0));
		setFocusable(false);
		setFocusableWindowState(false);
		setVisible(true);
		
		background = new BufferedImage(765, 503, BufferedImage.TYPE_INT_ARGB);
		g = background.createGraphics();
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				setLocation(game.getLocationOnScreen());
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, this);
	}
	
	public Graphics2D getPaintGraphics() {
		return g;
	}

}
