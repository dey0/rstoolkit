package rstoolkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rstoolkit.injection.Hooks;
import rstoolkit.injection.Injector;

public class Boot {

	private static Map<String, Class<?>> supportedGames = new HashMap<>();
	private static Map<String, String> supportedConfigs = new HashMap<>();

	private static Class<?> mainClass;
	private static String game;
	
	public static void main(String[] args) throws IOException {
		String game = args.length < 1 ? "runescape" : args[0];
		String config = supportedConfigs.get(game);
		if (config == null)
			config = "http://www.runescape.com/k=3/jav_config.ws";
		args = new String[] {
			"java",
			"-javaagent:" + new File("RSToolkit.jar").getAbsolutePath() + "=" + game,
			"-Djava.class.path=jagexappletviewer.jar",
			"-Dcom.jagex.config=" + config,
			"jagexappletviewer",
			"runescape"
		};
		File bin = new File(System.getProperty("user.home"), "/jagexcache/jagexlauncher/bin");
		File jav = new File(bin, "jagexappletviewer.jar");
		if (!jav.exists()) {
			bin.mkdirs();
			System.out.println("jagexappletviewer.jar not present. Downloading...");
			InputStream in = new URL("http://puu.sh/vShhW/c1144ec90b.jar").openStream();
			FileOutputStream fos = new FileOutputStream(jav);
			int read;
			byte[] buf = new byte[8192];
			while ((read = in.read(buf)) != -1)
				fos.write(buf, 0, read);
			fos.close();
		}
		System.out.println(bin);
		System.out.println(Arrays.toString(args));
		new ProcessBuilder(args).directory(bin).start();
	}

	public static void premain(String game, Instrumentation inst) throws IOException, InterruptedException {
		System.setProperty("java.vendor", "Oracle Corporation");
		System.setProperty("java.version", "1.7.0-internal");

		if (!supportedGames.containsKey(game)) {
			System.out.println("Unsupported game: " + game);
			return;
		}
		Boot.game = game;
		Boot.mainClass = supportedGames.get(game);
		Hooks.loadHooks(getMainClass().getPackage());
		File root = new File(Boot.class.getProtectionDomain().getCodeSource().getLocation().getFile());
		File workingDir = new File(System.getProperty("user.dir"));
		inst.addTransformer(new Injector(root, workingDir));
		System.out.println("RSToolkit initializing [" + game + "]...");
	}

	public static Class<?> getMainClass() {
		return mainClass;
	}

	public static String getGame() {
		return game;
	}

	static {
		supportedConfigs.put("runescape", "http://www.runescape.com/k=3/l=0/jav_config.ws");
		supportedConfigs.put("oldschool", "http://oldschool.runescape.com/jav_config.ws");
		supportedGames.put("runescape", rstoolkit.api.rs3.Client.class);
		supportedGames.put("oldschool", rstoolkit.api.osrs.Client.class);
	}

}
