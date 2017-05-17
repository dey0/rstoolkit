package rstoolkit.api.osrs;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("NpcDefinition")
public interface NpcDef {

	@Getter("name")
	public String getName();
	
}
