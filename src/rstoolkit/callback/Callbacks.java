package rstoolkit.callback;

import rstoolkit.client.Rs2Context;
import rstoolkit.client.Rs3Context;

public class Callbacks {

	public static void onLoad(rstoolkit.api.osrs.Client instance) {
		try {
			Rs2Context.startRs2(instance);
		} catch (Throwable t) {
			System.out.println("Caught exception on callback");
			t.printStackTrace();
		}
	}

	public static void onLoad(rstoolkit.api.rs3.Client instance) {
		try {
			Rs3Context.startRs3(instance);
		} catch (Throwable t) {
			System.out.println("Caught exception on callback");
			t.printStackTrace();
		}
	}
	
	public static void onPaint() {
		try {
			if (Rs2Context.instance != null) {
				Rs2Context.instance.onPaint();
			}
		} catch (Throwable t) {
			System.out.println("Caught exception on callback");
			t.printStackTrace();
		}
	}
	
	private static int prevAvail = 0;
	public static void onPacket(int avail) {
		try {
			if (prevAvail > 0 && avail == 0) {
				Rs2Context.instance.onTick();
			}
			prevAvail = avail;
		} catch (Throwable t) {
			System.out.println("Caught exception on callback");
			t.printStackTrace();
		}
	}

}
