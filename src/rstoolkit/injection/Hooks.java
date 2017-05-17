package rstoolkit.injection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rstoolkit.Boot;
import rstoolkit.injection.Rs2Hooks.ClassHook;
import rstoolkit.injection.Rs2Hooks.FieldHook;

public class Hooks {

	private static Map<String, String> mapping = new HashMap<>();
	private static Map<String, String> reverse = new HashMap<>();
	private static Map<String, ClassHook> classHooks = new HashMap<>();
	private static Map<String, FieldHook> fieldHooks = new HashMap<>();
	private static int version;
	
	public static void loadHooks(Package accessorPackage) throws IOException {
		Rs2Hooks hooks = Rs2Hooks.load(Hooks.class.getResourceAsStream("/hooks_" + Boot.getGame() + ".json"));
		version = hooks.getVersion();
		System.out.println("Loaded " + hooks.getClassHooks().length + " class hooks and " + hooks.getFieldHooks().length + " field hooks for #" + version);
		String pkg = accessorPackage.getName() + '.';
		for (ClassHook ch : hooks.getClassHooks()) {
			reverse.put(ch.getObfuscatedName(), pkg + ch.getName());
			mapping.put(pkg + ch.getName(), ch.getObfuscatedName());
			classHooks.put(pkg + ch.getName(), ch);
		}
		for (FieldHook fh : hooks.getFieldHooks()) {
			String ident = pkg + fh.getOwner() + '.' + fh.getName();
			mapping.put(ident, fh.getObfuscatedName());
			fieldHooks.put(ident, fh);
		}
	}

	public static String getAccessor(String className) {
		return reverse.get(className);
	}

	public static Map<String, String> getMapping() {
		return mapping;
	}

	public static FieldHook getFieldAccessor(String name, String fieldName) {
		return fieldHooks.get(name + '.' + fieldName);
	}
}
