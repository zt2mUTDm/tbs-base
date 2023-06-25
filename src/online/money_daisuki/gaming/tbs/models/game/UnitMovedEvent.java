package online.money_daisuki.gaming.tbs.models.game;

import java.util.Map;

import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public interface UnitMovedEvent {
	
	boolean wasSuccessful();
	
	Map<Integer, TileState> getChangedTiles();
	
	Map<Integer, Unit> getNewUnits();
	
	Integer getActualTile();
	
}
