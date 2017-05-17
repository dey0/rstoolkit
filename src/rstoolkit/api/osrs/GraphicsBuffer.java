package rstoolkit.api.osrs;

import java.awt.Image;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("GraphicsBuffer")
public interface GraphicsBuffer {

	@Getter("image")
	public Image getImage();
	
}
