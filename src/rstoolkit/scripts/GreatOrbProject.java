package rstoolkit.scripts;

import java.awt.Graphics2D;
import java.util.Arrays;

import rstoolkit.api.rs3.Client;
import rstoolkit.api.rs3.Npc;
import rstoolkit.api.rs3.WidgetComponent;
import rstoolkit.client.Rs3Context;
import rstoolkit.client.scripting.Script;

public class GreatOrbProject extends Script<Rs3Context> {

	private Client client;
	public GreatOrbProject(Rs3Context asd) {
		super(asd);
		this.client = asd.getClient();
	}

	private static final int WIDGET_ID = 781;
	
	private int myScore = -1, opponentScore = -1;
	private Altar altar;
	private int[] myScores = new int[6];
	private int[] opponentScores = new int[6];
	
	public enum Altar {
		NONE(-1), WAITING(-1), LOBBY(-1),
		PRE_AIR(0), AIR(0), POST_AIR(0),
		PRE_MIND(1), MIND(1), POST_MIND(1),
		PRE_WATER(2), WATER(2), POST_WATER(2),
		PRE_EARTH(3), EARTH(3), POST_EARTH(3), 
		PRE_FIRE(4), FIRE(4), POST_FIRE(4),
		PRE_BODY(5), BODY(5), POST_BODY(5),
		PRE_CHAOS(6), CHAOS(6), POST_CHAOS(6),
		PRE_NATURE(7), NATURE(7), POST_NATURE(7);
		int altarId;
		Altar(int altarId) {
			this.altarId = altarId;
		}
	}
	
	public enum Team {
		NONE("none", -1, -1),
		ACANTHA("green", 38, 39),
		VIEF("yellow", 47, 48);
		
		private String color;
		private int homeCompId;
		private int awayCompId;
		Team(String color, int homeCompId, int awayCompId) {
			this.color = color;
			this.homeCompId = homeCompId;
			this.awayCompId = awayCompId;
		}
		
		public String getColor() {
			return color;
		}
	}

	@Override
	public void init() {
		System.out.println("Great Orb Project initialized");
	}

	@Override
	public void onTick() {
		Team team = getTeam();
		Altar altar = getAltar();
		if (this.altar != altar) {
			System.out.println("Changed altar: " + altar + "; team: " + team);
			if (altar.altarId == 6) {
				System.out.println("Initializing members GOP...");
				myScores = Arrays.copyOf(myScores, 8);
				opponentScores = Arrays.copyOf(opponentScores, 8);
			}
			if (altar.ordinal() > 2 && altar.ordinal() % 3 == 2) {
				System.out.println("Altar finished. Score: " + getMyScore() + "-" + getOpponentScore());
				myScores[this.altar.altarId] = getMyScore();
				opponentScores[this.altar.altarId] = getOpponentScore();
			}
			this.altar = altar;
		}
		if (team != Team.NONE) {
			int myScore = getMyScore();
			int opponentScore = getOpponentScore();
			if (this.myScore != myScore || this.opponentScore != opponentScore) {
				System.out.println("Score: " + myScore + "-" + opponentScore);
				this.myScore = myScore;
				this.opponentScore = opponentScore;
			}
		} else if (myScore != -1 || opponentScore != -1) {
			gameEnded();
			myScore = -1;
			opponentScore = -1;
		}
		for (int i = 0; i < client.getNpcs().length; i++) {
			if (client.getNpcs()[i] != null) {
				Npc npc = (Npc) client.getNpcs()[i].getValue();
				if ("Yellow orb".equals(npc.getName())) {
					
				}
			}
		}
	}
	
	private void gameEnded() {
		System.out.println("Game ended.");
		StringBuilder str = new StringBuilder("My scores:       ");
		str.append(Arrays.toString(myScores));
		int go = computeGO(myScores);
		if (go != 0)
			str.append("  (").append(go).append(" GO)");
		System.out.println(str);
		str = new StringBuilder("Opponent scores: ");
		str.append(Arrays.toString(myScores));
		go = computeGO(myScores);
		if (go != 0) 
			str.append("  (").append(go).append(" GO)");
		System.out.println(str);
	}

	private int computeGO(int[] scores) {
		int go = 0;
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] == 0)
				return 0;
			go += scores[i];
		}
		return go;
	}
	
	private Team getTeam() {
		WidgetComponent greenHome = client.getComponent(WIDGET_ID, 38);
		return greenHome == null ? Team.NONE : (greenHome.isVisible() ? Team.ACANTHA : Team.VIEF);
	}
	
	private Altar getAltar() {
		WidgetComponent waitingComponent = client.getComponent(WIDGET_ID, 56);
		if (waitingComponent == null)
			return Altar.NONE;
		int offset = 0;
		if (waitingComponent.isVisible()) {
			switch (waitingComponent.getText()) {
			case "Waiting for players to join.": return Altar.WAITING;
			case "Waiting for portal to open.": offset = 1; break;
			case "Waiting for orbs to appear.": offset = 2; break;
			}
		}
		int altar = 0;
		while (client.getComponent(WIDGET_ID, 15 + altar).getModelId() != -1) altar++;
		
		return Altar.values()[altar * 3 + offset + 1];
	}
	
	private WidgetComponent getHomeComponent() {
		return client.getComponent(WIDGET_ID, getTeam().homeCompId);
	}
	
	private WidgetComponent getAwayComponent() {
		return client.getComponent(WIDGET_ID, getTeam().awayCompId);
	}
	
	private int getMyScore() {
		return Integer.parseInt(getHomeComponent().getText());
	}
	
	private int getOpponentScore() {
		return Integer.parseInt(getAwayComponent().getText());
	}
	
	public static boolean isActive() {
		return Rs3Context.getInstance().getClient().getComponent(WIDGET_ID, 38) != null;
	}

	@Override
	public void onPaint(Graphics2D g) {
	}
	
}
