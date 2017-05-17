package rstoolkit;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class BasicOSRSLoader implements AppletStub {

	private static URL codebase;
	private static Map<String, String> cfg = new HashMap<>(), params = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		codebase = new URL(args.length == 0 ? "http://oldschool94.runescape.com" : args[0]);
		parseParams(new URL(codebase, "/jav_config.ws").openStream());
		
		Applet applet = (Applet) new URLClassLoader(new URL[] { new File("oldschool140.jar").toURI().toURL() }).loadClass("client").newInstance();
		applet.setPreferredSize(new Dimension(765, 503));
		applet.setStub(new BasicOSRSLoader());
		
		Panel panel = new Panel();
		panel.setLayout(new BorderLayout());
		panel.add(applet);
		
		Frame f = new Frame("Jagex");
		f.add(panel);
		f.pack();
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				f.dispose();
				System.exit(0);
			}
		});
		
		applet.init();
	}

	private static void parseParams(InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line;
		while ((line = br.readLine()) != null) {
			int idx = line.indexOf('=');
			if (idx != -1) {
				String key = line.substring(0, idx);
				String val = line.substring(idx + 1);
				if (key.equals("param")) {
					idx = val.indexOf('=');
					key = val.substring(0, idx);
					val = val.substring(idx + 1);
					params.put(key, val);
				} else {
					cfg.put(key, val);
				}
			}
		}
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public URL getDocumentBase() {
		return codebase;
	}

	@Override
	public URL getCodeBase() {
		return codebase;
	}

	@Override
	public String getParameter(String name) {
		return params.get(name);
	}

	@Override
	public AppletContext getAppletContext() {
		return null;
	}

	@Override
	public void appletResize(int width, int height) {
	}

}
