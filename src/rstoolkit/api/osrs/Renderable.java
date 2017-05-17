package rstoolkit.api.osrs;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("Renderable")
public interface Renderable extends Cacheable {

	@Getter("modelHeight")
	public int getModelHeight();
	
}
