package rstoolkit.api.osrs;

import java.awt.Point;

import rstoolkit.client.Rs2Context;
import rstoolkit.client.scripting.Clickable;
import rstoolkit.client.scripting.Util;
import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("Actor")
public interface Actor extends Renderable, Clickable {

	@Getter("x")
	public int getX();
	
	@Getter("y")
	public int getY();
	
	public String getName();
	
	@Override
	default Point getPointOnScreen() {
		return Util.rasterize(Rs2Context.instance.getClient(), getX(), getY(), getModelHeight() / 2);
	}
	
}
