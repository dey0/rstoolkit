package rstoolkit.client.scripting;

import java.awt.Point;

import rstoolkit.api.osrs.Client;

public class Util {

	public static Point rasterize(Client c, int x, int y, int z) {
		if (x >= 128 && y >= 128 && x <= 13056 && y <= 13056) {
			z = getTileHeight(c, x, y, c.getPlane()) - z;
			x -= c.getCameraX();
			z -= c.getCameraZ();
			y -= c.getCameraY();
			int sinPitch = c.getSineTable()[c.getCameraPitch()];
			int cosPitch = c.getCosineTable()[c.getCameraPitch()];
			int sinYaw = c.getSineTable()[c.getCameraYaw()];
			int cosYaw = c.getCosineTable()[c.getCameraYaw()];
			int tmp = cosYaw * x + y * sinYaw >> 16;
			y = cosYaw * y - sinYaw * x >> 16;
			x = tmp;
			tmp = cosPitch * z - y * sinPitch >> 16;
			y = z * sinPitch + y * cosPitch >> 16;
			if (y >= 50) {
				x = c.getViewportWidth() / 2 + x * c.getCameraZoom() / y;
				y = c.getViewportHeight() / 2 + tmp * c.getCameraZoom() / y;
				return new Point(x + 4, y + 4);
			}
		}
		return new Point(-1, -1);
	}

	public static int getTileHeight(Client c, int var0, int var1, int var2) {
		int var3 = var0 >> 7;
		int var4 = var1 >> 7;
		if (var3 >= 0 && var4 >= 0 && var3 <= 103 && var4 <= 103) {
			int var5 = var2;
			if (var2 < 3 && (c.getTileSettings()[1][var3][var4] & 0x2) == 2) {
				var5 = var2 + 1;
			}

			int var6 = var0 & 0x7f;
			int var7 = var1 & 0x7f;
			int var8 = var6 * c.getTileHeights()[var5][var3 + 1][var4]
					+ (128 - var6) * c.getTileHeights()[var5][var3][var4] >> 7;
			int var9 = c.getTileHeights()[var5][var3][1 + var4] * (128 - var6)
					+ var6 * c.getTileHeights()[var5][var3 + 1][var4 + 1] >> 7;
			return (128 - var7) * var8 + var7 * var9 >> 7;
		}
		return 0;
	}

}
