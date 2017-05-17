package rstoolkit.scripts.debug;

import rstoolkit.api.osrs.Client;
import rstoolkit.client.Rs2Context;
import rstoolkit.client.scripting.DebugString;

public class MouseDebug extends DebugString {

	@Override
	public String debug() {
		Client client = Rs2Context.instance.getClient();
		return Integer.toString(client.getState());
	}
	
	@Override
	public String getDesc() {
		return "Mouse";
	}
	
}
