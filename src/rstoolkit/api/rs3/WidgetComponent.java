package rstoolkit.api.rs3;

import rstoolkit.client.Rs3Context;
import rstoolkit.injection.Accessor;
import rstoolkit.injection.Getter;
import rstoolkit.injection.Setter;

@Accessor("WidgetComponent")
public interface WidgetComponent {

	@Getter("text")
	public String getText();
	
	@Setter("text")
	public void setText(String text);

	@Getter("options")
	public String[] getOptions();

	@Getter("hidden")
	public boolean isHidden();

	@Getter("parentId")
	public int getParentId();

	@Getter("modelId")
	public int getModelId();

	default boolean isVisible() {
		if (isHidden()) return false;
		int parentId = getParentId();
		if (parentId == -1) return true;
		if (parentId == 0) return false;
		Client client = Rs3Context.getInstance().getClient();
		WidgetComponent parent = client.getComponent(parentId >> 16, parentId & 0xffff);
		return parent.isVisible();
	}

}
