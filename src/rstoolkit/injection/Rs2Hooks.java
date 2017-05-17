package rstoolkit.injection;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class Rs2Hooks {

	private String game;
	private int version;
	private ClassHook[] classHooks;
	private FieldHook[] fieldHooks;
	
	public static class ClassHook {
		private String obfuscatedName;
		private String name;
		
		public String getObfuscatedName() {
			return obfuscatedName;
		}
		
		public String getName() {
			return name;
		}

		public void setObfuscatedName(String obfuscatedName) {
			this.obfuscatedName = obfuscatedName;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	public static class FieldHook {
		private String obfuscatedOwner;
		private String obfuscatedName;
		private String owner;
		private String name;
		private Number multiplier;
		
		public String getObfuscatedOwner() {
			return obfuscatedOwner;
		}
		
		public String getObfuscatedName() {
			return obfuscatedName;
		}
		
		public String getOwner() {
			return owner;
		}
		
		public String getName() {
			return name;
		}
		
		public void setObfuscatedOwner(String obfuscatedOwner) {
			this.obfuscatedOwner = obfuscatedOwner;
		}
		
		public void setObfuscatedName(String obfuscatedName) {
			this.obfuscatedName = obfuscatedName;
		}
		
		public void setOwner(String owner) {
			this.owner = owner;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setMultiplier(Number multiplier) {
			this.multiplier = multiplier;
		}
		
		public boolean hasObfuscatedOwner() {
			return obfuscatedOwner != null;
		}
		
		public boolean hasMultiplier() {
			return multiplier != null;
		}
		
		public int getMultiplier() {
			return multiplier.intValue();
		}
	}
	
	public String getGame() {
		return game;
	}
	
	public int getVersion() {
		return version;
	}
	
	public ClassHook[] getClassHooks() {
		return classHooks;
	}
	
	public FieldHook[] getFieldHooks() {
		return fieldHooks;
	}
	
	public void setClassHooks(ClassHook[] classHooks) {
		this.classHooks = classHooks;
	}

	public static Rs2Hooks load(InputStream in) {
		Gson g = new Gson();
		Rs2Hooks hooks = g.fromJson(new InputStreamReader(in), Rs2Hooks.class);
		return hooks;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public void setFieldHooks(FieldHook[] fieldHooks) {
		this.fieldHooks = fieldHooks;
	}

}
