package rstoolkit.api.osrs;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("Player")
public interface Player extends Actor {

	@Getter("displayName")
	public String getDisplayName();
	
}
