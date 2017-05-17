package rstoolkit.client;

import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import rstoolkit.Boot;
import rstoolkit.api.osrs.Client;
import rstoolkit.api.osrs.GraphicsBuffer;
import rstoolkit.client.scripting.DebugOverlay;
import rstoolkit.client.scripting.DebugString;
import rstoolkit.client.scripting.Mouse;
import rstoolkit.client.scripting.Overlay;
import rstoolkit.client.scripting.Script;
import rstoolkit.scripts.debug.CameraDebug;
import rstoolkit.scripts.debug.GameStateDebug;
import rstoolkit.scripts.debug.LocationDebug;
import rstoolkit.scripts.debug.NpcDebug;
import rstoolkit.scripts.debug.PlayerDebug;

public class Rs2Context implements Runnable {

	public static Rs2Context instance;
	private Client client;
	private Graphics2D imageGraphics;
	private GraphicsBuffer graphicsBuffer;
	private Mouse mouse;
	private Map<String, DebugString> debugStrings = new HashMap<>();
	private Map<String, Overlay> overlays = new HashMap<>();
	private List<DebugString> debugs;
	private List<DebugOverlay> debugOverlays;
	private Script<Rs2Context> runningScript;
	
	public Rs2Context(Client client) {
		this.client = client;
		this.mouse = new Mouse(client);
		debugs = new ArrayList<>();
		debugs.add(new LocationDebug());
		debugs.add(new CameraDebug());
		debugs.add(new GameStateDebug());
		debugOverlays = new ArrayList<>();
		debugOverlays.add(new PlayerDebug());
		debugOverlays.add(new NpcDebug());
	}

	@Override
	public void run() {
		Panel panel = (Panel) client.asApplet().getParent();
		Frame frame = (Frame) panel.getParent();
		frame.setTitle("RSToolkit");
		try {
			frame.setIconImage(ImageIO.read(Rs3Context.class.getClassLoader().getResourceAsStream("toolkit.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		MenuBar bar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem newRs3 = new MenuItem("New RS3");
		newRs3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Boot.main(new String[] { "runescape" });
				} catch (IOException e1) {
				}
			}
		});
		MenuItem newOsrs = new MenuItem("New OSRS");
		newOsrs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Boot.main(new String[] { "oldschool" });
				} catch (IOException e1) {
				}
			}
		});
		fileMenu.add(newRs3);
		fileMenu.add(newOsrs);
		Menu debugMenu = new Menu("Debug");
		for (DebugString debug : debugs) {
			CheckboxMenuItem mi = new CheckboxMenuItem(debug.getDesc());
			mi.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (debugStrings.containsKey(debug.getDesc()))
						debugStrings.remove(debug.getDesc());
					else
						debugStrings.put(debug.getDesc(), debug);
				}
			});
			debugMenu.add(mi);
		}
		for (DebugOverlay debug : debugOverlays) {
			CheckboxMenuItem mi = new CheckboxMenuItem(debug.getDesc());
			mi.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (overlays.containsKey(debug.getDesc()))
						overlays.remove(debug.getDesc());
					else
						overlays.put(debug.getDesc(), debug);
				}
			});
			debugMenu.add(mi);
		}
		bar.add(debugMenu);
		frame.setMenuBar(bar);
		frame.pack();
		try {
			while (true) {
				loop();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void loop() throws Exception {
		Thread.sleep(50);
	}

	public static void startRs2(Client instance) {
		System.out.println("Starting RS2 toolkit");
		Rs2Context.instance = new Rs2Context(instance);
		new Thread(Rs2Context.instance).start();
	}
	
	public void onPaint() {
		if (graphicsBuffer != client.getGraphicsBuffer()) {
			graphicsBuffer = client.getGraphicsBuffer();
			imageGraphics = (Graphics2D) graphicsBuffer.getImage().getGraphics();
		}
		imageGraphics.setPaint(Color.RED);
		imageGraphics.drawLine(client.getMouseX(), 0, client.getMouseX(), client.getCanvas().getHeight());
		imageGraphics.drawLine(0, client.getMouseY(), client.getCanvas().getWidth(), client.getMouseY());
		List<String> text = new ArrayList<>();
		for (DebugString ds : debugStrings.values()) {
			text.add(ds.debug());
		}
		int liney = 35;
		for (String line : text) {
			imageGraphics.drawString(line, 20, liney);
			liney += 15;
		}
		for (Overlay overlay : overlays.values()) {
			overlay.onPaint(imageGraphics);
		}
	}

	public void onTick() {
		if (runningScript != null)
			runningScript.onTick();
	}
	
	public Client getClient() {
		return client;
	}
	
	public Mouse getMouse() {
		return mouse;
	}

}
