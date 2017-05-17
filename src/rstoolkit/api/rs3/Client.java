package rstoolkit.api.rs3;

import java.applet.Applet;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("Client")
public interface Client  {
	
	@Getter("widgetCache")
	public Widget[] getWidgetCache();
	
	@Getter("validWidgetComponents")
	public boolean[] getValidWidgetComponents();

	@Getter("applet")
	public Applet getApplet();
	
	@Getter("npcs")
	public NpcWrapper[] getNpcs();
	
	default Widget getWidget(int widgetId) {
		Widget[] widgets = getWidgetCache();
		if (widgets == null || widgetId >= widgets.length) {
			return null;
		}
		return widgets[widgetId];
	}
	
	default WidgetComponent getComponent(int widgetId, int componentId) {
		Widget widget = getWidget(widgetId);
		if (widget == null)
			return null;
		WidgetComponent[] components = widget.getComponents();
		if (components == null || componentId >= components.length)
			return null;
		return components[componentId];
	}
	
}
