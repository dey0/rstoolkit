package rstoolkit.api.osrs;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("Npc")
public interface Npc extends Actor {

	@Getter("def")
	public NpcDef getDefinition();
	
	@Override
	default String getName() {
		if (getDefinition() == null)
			return null;
		return getDefinition().getName();
	}
	
}
