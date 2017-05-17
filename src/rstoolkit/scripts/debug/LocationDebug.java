package rstoolkit.scripts.debug;

import rstoolkit.api.osrs.Client;
import rstoolkit.client.Rs2Context;
import rstoolkit.client.scripting.DebugString;

public class LocationDebug extends DebugString {

	@Override
	public String getDesc() {
		return "Location";
	}

	@Override
	public String debug() {
		Client c = Rs2Context.instance.getClient();
		if (c.getLocalPlayer() != null) {
			int x = (c.getLocalPlayer().getX() >> 7) + c.getBaseX();
			int y = (c.getLocalPlayer().getY() >> 7) + c.getBaseY();
			return "Location: (" + x + ", " + y + ")";
		}
		return "Location: none";
	}

}
