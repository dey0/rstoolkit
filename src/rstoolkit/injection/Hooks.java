package rstoolkit.injection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rstoolkit.Boot;
import rstoolkit.injection.Rs2Hooks.ClassHook;
import rstoolkit.injection.Rs2Hooks.FieldHook;
import rstoolkit.injection.Rs2Hooks.HookReport;

public class Hooks {

	private static Map<String, String> mapping = new HashMap<>();
	private static Map<String, String> reverse = new HashMap<>();
	private static Map<String, ClassHook> classHooks = new HashMap<>();
	private static Map<String, FieldHook> fieldHooks = new HashMap<>();
	private static int version;
	
	public static void loadHooks(Package accessorPackage) throws IOException {
		HookReport report = HookReport.parseFrom(Hooks.class.getResourceAsStream("/hooks_" + Boot.getGame() + ".pb"));
		version = report.getRunescapeVersion();
		System.out.println("Loaded " + report.getClassHooksCount() + " class hooks and " + report.getFieldHooksCount() + " field hooks for #" + version);
		String pkg = accessorPackage.getName() + '.';
		for (ClassHook ch : report.getClassHooksList()) {
			reverse.put(ch.getObfuscatedName(), pkg + ch.getName());
			mapping.put(pkg + ch.getName(), ch.getObfuscatedName());
			classHooks.put(pkg + ch.getName(), ch);
		}
		for (FieldHook fh : report.getFieldHooksList()) {
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
