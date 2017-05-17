package rstoolkit.client.scripting;

public abstract class Script<T> extends Overlay {
	
	protected T t;
	
	public Script(T asd) {
		this.t = asd;
	}
	
	public abstract void init();
	public abstract void onTick();
	
}
