package rstoolkit.client.scripting;

import java.awt.Component;
import java.awt.event.MouseEvent;

import rstoolkit.api.osrs.Client;
import rstoolkit.api.osrs.MouseHandler;

public class Mouse {

	private Client client;
	private MouseHandler handler;
	private Component target;
	
	public Mouse(Client client) {
		this.client = client;
		this.handler = client.getMouseHandler();
	}

	private void submitClick(int dstX, int dstY, int button) {
		this.handler = client.getMouseHandler();
		checkTarget();
		if (target == null) return;
		try {
			int srcX = client.getMouseX();
			int srcY = client.getMouseY();
			int numPoints = 12;
			for (int i = 1; i < numPoints; i++) {
				int x = srcX + (dstX - srcX) * i / numPoints;
				int y = srcY + (dstY - srcY) * i / numPoints;
				handler.mouseMoved(new MouseEvent(target, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false, 0));
				Thread.sleep(25);
			}
			handler.mouseMoved(new MouseEvent(target, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, dstX, dstY, 0, false, 0));
			Thread.sleep(40);
			handler.mousePressed(new MouseEvent(target, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, dstX, dstY, 0, false, button));
			Thread.sleep(100);
			handler.mouseReleased(new MouseEvent(target, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, dstX, dstY, 0, false, button));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void checkTarget() {
		if (target == null) {
			target = client.asApplet().getComponent(0);
		}
	}

	public void submitLeftClick(int dstX, int dstY) {
		submitClick(dstX, dstY, MouseEvent.BUTTON1);
	}

	public void submitRightClick(int dstX, int dstY) {
		submitClick(dstX, dstY, MouseEvent.BUTTON3);
	}
	
}
