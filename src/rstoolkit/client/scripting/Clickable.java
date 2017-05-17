package rstoolkit.client.scripting;

import java.awt.Point;

import rstoolkit.client.Rs2Context;

public interface Clickable {

	default void click() {
		Point p = getPointOnScreen();
		Mouse mouse = Rs2Context.instance.getMouse();
		mouse.submitLeftClick(p.x, p.y);
	}
	
	public Point getPointOnScreen();
	
}
