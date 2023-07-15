package online.money_daisuki.gaming.tbs.models.game.network;

import java.util.Arrays;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.game.Player;

public final class LevelDataModel {
	private final String levelName;
	//private byte[] levelHash;
	private final String packName;
	//private byte[] packHash;
	private final Player[] players;
	
	public LevelDataModel(final String levelName, final String packName, final Player[] players) {
		this.levelName = Requires.notNull(levelName, "levelName == null");
		this.packName = Requires.notNull(packName, "packName == null");
		this.players = Requires.containsNotNull(Arrays.copyOf(players, Requires.notNull(players, "players == null").length));
	}
	public String getLevelName() {
		return (levelName);
	}
	public String getPackName() {
		return (packName);
	}
		public Player[] getPlayers() {
		return(Arrays.copyOf(players, players.length));
	}
}
