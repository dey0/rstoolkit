package rstoolkit.api.rs3;

import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;

@Accessor("Widget")
public interface Widget {

	@Getter("components")
	public WidgetComponent[] getComponents();
	
}
