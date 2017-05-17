package rstoolkit.api.rs3;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("NpcWrapper")
public interface NpcWrapper {

	@Getter("value")
	public Object getValue();
	
}
