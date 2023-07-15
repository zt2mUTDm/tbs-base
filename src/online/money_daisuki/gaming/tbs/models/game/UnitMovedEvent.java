package online.money_daisuki.gaming.tbs.models.game;

import java.io.Serializable;
import java.util.Map;

import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public interface UnitMovedEvent extends Serializable {
	
	boolean wasSuccessful();
	
	Map<Integer, TileState> getChangedTiles();
	
	Map<Integer, Unit> getNewUnits();
	
	Integer getTargetTile();
	
	boolean hasAttackEvent();
	
	UnitAttackedResponse getAttackEvent();
	
}
