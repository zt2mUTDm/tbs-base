package online.money_daisuki.gaming.tbs.models.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public final class GameDataReponse implements Serializable {
	private final Map<Integer, TileState> tileStates;
	private final Map<Integer, Unit> visibleUnits;
	
	public GameDataReponse(final Map<Integer, TileState> tileStates, final Map<Integer, Unit> visibleUnits) {
		// TODO
		this.tileStates = tileStates;
		this.visibleUnits = Requires.containsNotNull(new HashMap<>(Requires.notNull(visibleUnits)));
	}
	
	public Map<Integer, Unit> getVisibleUnits() {
		return(new HashMap<>(visibleUnits));
	}
	public Map<Integer, TileState> getTileStates() {
		return(new HashMap<>(tileStates));
	}
}
