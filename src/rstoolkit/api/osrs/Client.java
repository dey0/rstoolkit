package rstoolkit.api.osrs;

import java.applet.Applet;
import java.awt.Canvas;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;
import rstoolkit.injection.Setter;

@Accessor("Client")
public interface Client extends GameShell {
	
	@Getter("localPlayer")
	public Player getLocalPlayer();

	@Getter("players")
	public Player[] getPlayers();

	@Getter("npcs")
	public Npc[] getNpcs();

	@Getter("plane")
	public int getPlane();

	@Getter("cameraYaw")
	public int getCameraYaw();

	@Getter("cameraPitch")
	public int getCameraPitch();

	@Getter("cameraX")
	public int getCameraX();

	@Getter("cameraY")
	public int getCameraY();

	@Getter("cameraZ")
	public int getCameraZ();

	@Getter("zoom")
	public int getCameraZoom();

	@Setter("zoom")
	public void setCameraZoom(int cameraZoom);

	@Getter("viewportX")
	public int getViewportWidth();

	@Getter("viewportY")
	public int getViewportHeight();

	@Getter("tileHeights")
	public int[][][] getTileHeights();

	@Getter("tileSettings")
	public byte[][][] getTileSettings();
	
	@Getter("baseX")
	public int getBaseX();
	
	@Getter("baseY")
	public int getBaseY();

	@Getter("gameBuffer")
	public GraphicsBuffer getGraphicsBuffer();

	@Getter("state")
	public int getState();
	
	@Getter("SINE")
	public int[] getSineTable();
	
	@Getter("COSINE")
	public int[] getCosineTable();
	
	@Getter("mouseHandler")
	public MouseHandler getMouseHandler();
	
	@Getter("clickState")
	public int getClickState();
	
	@Getter("mouseX")
	public int getMouseX();
	
	@Getter("mouseY")
	public int getMouseY();
	
	@Getter("mousePressMod")
	public int getMousePressMod();
	
	@Getter("mousePressX")
	public int getMousePressX();
	
	@Getter("mousePressY")
	public int getMousePressY();
	
	@Getter("mousePressTime")
	public int getMousePressTime();
	
	default Applet asApplet() {
		return (Applet) this;
	}
	
	default Canvas getCanvas() {
		if (asApplet().getComponentCount() > 0)
			return (Canvas) asApplet().getComponent(0);
		return null;
	}
	
}
