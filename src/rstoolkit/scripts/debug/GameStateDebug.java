package rstoolkit.scripts.debug;

import rstoolkit.client.Rs2Context;
import rstoolkit.client.scripting.DebugString;

public class GameStateDebug extends DebugString {

	@Override
	public String debug() {
		return "Game state: " + Rs2Context.instance.getClient().getState();
	}
	
	@Override
	public String getDesc() {
		return "Game state";
	}
	
}
