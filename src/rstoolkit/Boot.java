package rstoolkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rstoolkit.client.ConsoleOutputStream;
import rstoolkit.injection.Hooks;
import rstoolkit.injection.Injector;

public class Boot {

	private static Map<String, Class<?>> supportedGames = new HashMap<>();
	private static Map<String, String> supportedConfigs = new HashMap<>();

	private static Class<?> mainClass;
	private static String game;
	private static File root;
	private static ConsoleOutputStream standardOut;
	
	public static void main(String[] args) throws IOException {
		String game = args.length < 1 ? "runescape" : args[0];
		String config = supportedConfigs.get(game);
		if (config == null)
			config = "http://www.runescape.com/k=3/jav_config.ws";
		String classpath = "";
		root = new File(Boot.class.getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!root.toString().endsWith(".jar")) {
			classpath = ";" + System.getProperty("java.class.path");
			root = new File(root.getParentFile(), "agent.jar");
		}
		args = new String[] {
			"java",
			"-javaagent:" + root.getAbsolutePath() + "=" + game,
			"-Djava.class.path=jagexappletviewer.jar" + classpath,
			"-Dcom.jagex.config=" + config,
			"jagexappletviewer",
			"runescape"
		};
		File bin = new File(System.getProperty("user.home"), "/jagexcache/jagexlauncher/bin");
		File jav = new File(bin, "jagexappletviewer.jar");
		if (!jav.exists()) {
			bin.mkdirs();
			System.out.println("jagexappletviewer.jar not present. Downloading...");
			HttpURLConnection con = (HttpURLConnection) new URL("http://puu.sh/vT82D.jar").openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/5.0");
			InputStream in = con.getInputStream();
			FileOutputStream fos = new FileOutputStream(jav);
			int read;
			byte[] buf = new byte[8192];
			while ((read = in.read(buf)) != -1)
				fos.write(buf, 0, read);
			fos.close();
		}
		System.out.println("Working directory: " + bin);
		System.out.println("Launch command: " + Arrays.toString(args));
		new ProcessBuilder(args).directory(bin).start();
	}

	public static void premain(String game, Instrumentation inst) throws IOException, InterruptedException {
		Boot.standardOut = new ConsoleOutputStream(System.out);
		System.setOut(new PrintStream(Boot.standardOut));
		System.setProperty("java.vendor", "Oracle Corporation");
		System.setProperty("java.version", "1.7.0-internal");

		if (!supportedGames.containsKey(game)) {
			System.out.println("Unsupported game: " + game);
			return;
		}
		Boot.game = game;
		Boot.mainClass = supportedGames.get(game);
		Boot.root = new File(Boot.class.getProtectionDomain().getCodeSource().getLocation().getFile());
		Hooks.loadHooks(getMainClass().getPackage());
		File workingDir = new File(System.getProperty("user.dir"));
		inst.addTransformer(new Injector(workingDir));
		System.out.println("RSToolkit initializing [" + game + "]...");
	}

	public static Class<?> getMainClass() {
		return mainClass;
	}

	public static String getGame() {
		return game;
	}
	
	public static ConsoleOutputStream getStandardOut() {
		return standardOut;
	}
	
	static {
		supportedConfigs.put("runescape", "http://www.runescape.com/k=3/l=0/jav_config.ws");
		supportedConfigs.put("oldschool", "http://oldschool.runescape.com/jav_config.ws");
		supportedGames.put("runescape", rstoolkit.api.rs3.Client.class);
		supportedGames.put("oldschool", rstoolkit.api.osrs.Client.class);
	}

}
