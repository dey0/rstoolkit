package rstoolkit.scripts.debug;

import rstoolkit.api.osrs.Client;
import rstoolkit.client.Rs2Context;
import rstoolkit.client.scripting.DebugString;

public class CameraDebug extends DebugString {

	@Override
	public String debug() {
		Client client = Rs2Context.instance.getClient();
		return "Camera X: " + client.getCameraX() + " Y: " + client.getCameraY() + " Z: " + client.getCameraZ() + " Zoom: " + client.getCameraZoom() + " Yaw: " + client.getCameraYaw() + " Pitch: " + client.getCameraPitch();
	}
	
	@Override
	public String getDesc() {
		return "Camera";
	}
	
}
