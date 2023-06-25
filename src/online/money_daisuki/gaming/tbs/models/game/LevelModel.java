package online.money_daisuki.gaming.tbs.models.game;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.gaming.tbs.models.data.Weather;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public interface LevelModel {
	
	// Tiles
	
	int getTileConnectionCount(Integer tileId);
	
	boolean isTileConnected(Integer tileId, int connectionId);
	
	int getConnectedTile(Integer tileId, int connectionId);
	
	int getTileType(Integer tileId);
	
	TileState getTileState(Integer tileId);
	
	TileState setTileState(Integer tileId, TileState state);
	
	TileState removeTileState(Integer tileId);
	
	void clearTileStates();
	
	// Units
	
	Unit addUnitToTile(Integer tileId, Unit unit);
	
	boolean isUnitOnTile(Integer tileId);
	
	Unit getUnitOnTile(Integer tileId);
	
	Unit removeUnitFromTile(Integer tileId);
	
	void clearUnits();
	
	// Player
	
	int getPlayerCount();
	
	Player getPlayer(int i);
	
	int getCurrentPlayerId();
	
	// Misc
	
	Weather getWeather();
	
	// Business
	
	void endTurn(DataSink<? super UnitEndEvent> callback);
	
}
