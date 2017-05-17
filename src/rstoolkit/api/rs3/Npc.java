package rstoolkit.api.rs3;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("Npc")
public interface Npc {

	@Getter("name")
	public String getName();
	
}
