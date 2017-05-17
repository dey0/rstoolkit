package rstoolkit.scripts.debug;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import rstoolkit.api.osrs.Player;
import rstoolkit.client.Rs2Context;
import rstoolkit.client.scripting.DebugOverlay;

public class PlayerDebug extends DebugOverlay {

	@Override
	public void onPaint(Graphics2D g) {
		Player[] players = Rs2Context.instance.getClient().getPlayers();
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				Point p = players[i].getPointOnScreen();
				if (players[i].getDisplayName() != null)
					drawCenteredString(players[i].getDisplayName(), p.x, p.y, g);
			}
		}
	}
	
	private void drawCenteredString(String str, int x, int y, Graphics g) {
		g.drawString(str, x - g.getFontMetrics().stringWidth(str) / 2, y);
	}

	@Override
	public String getDesc() {
		return "Player names";
	}
	
}
