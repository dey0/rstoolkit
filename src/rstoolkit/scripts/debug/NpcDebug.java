package rstoolkit.scripts.debug;

import java.awt.Graphics2D;
import java.awt.Point;

import rstoolkit.api.osrs.Client;
import rstoolkit.api.osrs.Npc;
import rstoolkit.client.Rs2Context;
import rstoolkit.client.scripting.DebugOverlay;
import rstoolkit.client.scripting.Util;

public class NpcDebug extends DebugOverlay {

	@Override
	public void onPaint(Graphics2D g) {
		Client client = Rs2Context.instance.getClient();
		for (Npc npc : client.getNpcs()) {
			if (npc != null) {
				int x = npc.getX() - 64;
				int y = npc.getY() - 64;
				int z = npc.getModelHeight();
				Point p0 = Util.rasterize(client, x, y, 0);
				Point p1 = Util.rasterize(client, x + 128, y, 0);
				Point p2 = Util.rasterize(client, x, y + 128, 0);
				Point p3 = Util.rasterize(client, x + 128, y + 128, 0);
				Point p4 = Util.rasterize(client, x, y, z);
				Point p5 = Util.rasterize(client, x + 128, y, z);
				Point p6 = Util.rasterize(client, x, y + 128, z);
				Point p7 = Util.rasterize(client, x + 128, y + 128, z);
				g.drawLine(p0.x, p0.y, p1.x, p1.y);
				g.drawLine(p1.x, p1.y, p3.x, p3.y);
				g.drawLine(p3.x, p3.y, p2.x, p2.y);
				g.drawLine(p2.x, p2.y, p0.x, p0.y);
				g.drawLine(p0.x, p0.y, p4.x, p4.y);
				g.drawLine(p1.x, p1.y, p5.x, p5.y);
				g.drawLine(p2.x, p2.y, p6.x, p6.y);
				g.drawLine(p3.x, p3.y, p7.x, p7.y);
				g.drawLine(p4.x, p4.y, p5.x, p5.y);
				g.drawLine(p5.x, p5.y, p7.x, p7.y);
				g.drawLine(p7.x, p7.y, p6.x, p6.y);
				g.drawLine(p6.x, p6.y, p4.x, p4.y);
			}
		}
	}
	
	@Override
	public String getDesc() {
		return "Npcs";
	}
	
}
