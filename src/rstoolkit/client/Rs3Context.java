package rstoolkit.client;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import rstoolkit.Boot;
import rstoolkit.api.rs3.Client;
import rstoolkit.api.rs3.Widget;
import rstoolkit.api.rs3.WidgetComponent;
import rstoolkit.client.scripting.Script;
import rstoolkit.scripts.GreatOrbProject;

public class Rs3Context implements Runnable {

	private static Rs3Context instance;
	private Client client;
	private Frame viewerFrame;
	private Script<Rs3Context> script;
	
	public Rs3Context(Client client) {
		Rs3Context.instance = this;
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		viewerFrame = (Frame) client.getApplet().getParent().getParent();
		try {
			viewerFrame.setIconImage(ImageIO.read(Rs3Context.class.getClassLoader().getResourceAsStream("toolkit.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		viewerFrame.setTitle("RSToolkit");
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
		bar.add(fileMenu);
		Menu debugMenu = new Menu("Debug");
		MenuItem mi = new MenuItem("Widgets");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createWidgetsDialog();
			}
		});
		debugMenu.add(mi);
		bar.add(debugMenu);
		viewerFrame.setMenuBar(bar);
		viewerFrame.pack();
		dialog = new PaintDialog(viewerFrame, client.getApplet());
		try {
			while (true) {
				loop();
				Thread.sleep(50);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void createWidgetsDialog() {
		DefaultMutableTreeNode top =
		        new DefaultMutableTreeNode("Widgets");
		Widget[] widgets = client.getWidgetCache();
		if (widgets != null) {
			System.out.println("Widgets: " + widgets + " size: " + widgets.length);
			for (int i = 0; i < widgets.length; i++) {
				if (widgets[i] != null) {
					DefaultMutableTreeNode widgetNode = new DefaultMutableTreeNode("Widget [" + i + "]");
					WidgetComponent[] components = widgets[i].getComponents();
					for (int j = 0; j < components.length; j++) {
						if (components[j] != null) {
							DefaultMutableTreeNode componentNode = new DefaultMutableTreeNode("[" + j + "] " + components[j].getText());
							widgetNode.add(componentNode);
						}
					}
					top.add(widgetNode);
				}
			}
		}
		JTree tree = new JTree(top);
		tree.setRootVisible(false);
		JScrollPane jsp = new JScrollPane(tree);
		jsp.setPreferredSize(new Dimension(500, 400));
		tree.expandPath(tree.getAnchorSelectionPath());
		JDialog frame = new JDialog(viewerFrame, "Widgets", false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(jsp);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private PaintDialog dialog;
	
	public void loop() {
		if (GreatOrbProject.isActive() && script == null) {
			script = new GreatOrbProject(this);
		} else if (!GreatOrbProject.isActive() && script != null) {
			script = null;
		}
		if (script != null) {
			script.onTick();
			script.onPaint(dialog.getPaintGraphics());
		}
		
		dialog.getPaintGraphics().drawString("Hello world", 30, 30);
		dialog.repaint();
	}
	
	public static void startRs3(Client instance) {
		System.out.println("Starting RS3 toolkit");
		new Thread(new Rs3Context(instance)).start();
	}
	
	public static Rs3Context getInstance() {
		return instance;
	}

	public Client getClient() {
		return client;
	}
	
}
