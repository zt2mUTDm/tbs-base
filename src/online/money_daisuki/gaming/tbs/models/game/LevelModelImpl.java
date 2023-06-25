package online.money_daisuki.gaming.tbs.models.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.data.Weather;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public final class LevelModelImpl implements LevelModel {
	private final int playerCount;
	private final TileField tiles;
	private final Map<Integer, Unit> tileIdToUnitMap;
	private final Player[] players;
	
	private Weather currentWeather;
	
	private int currentPlayerId;
	
	private final Map<Integer, TileState> tileStates;
	
	public LevelModelImpl(final int playerCount, final TileField tiles, final Map<Integer, Unit> tileIdToUnitMap,
			 final Weather currentWeather, final Player[] players, final Map<Player, FogOfWarModelImpl> fogOfWarModels) {
		this.playerCount = Requires.greaterThanZero(playerCount, "playerCount <= 0");
		this.tiles = Requires.notNull(tiles, "tiles == null");
		this.tileIdToUnitMap = Requires.containsNotNull(new HashMap<Integer, Unit>(Requires.containsNotNull(tileIdToUnitMap, "tileIdToUnitMap == null")));
		this.currentWeather = Requires.notNull(currentWeather, "currentWeather == null");
		this.players = Requires.containsNotNull(Arrays.copyOf(players, Requires.notNull(players, "players == null").length), "players contains null");
		
		this.tileStates = new HashMap<>();
	}
	
	// Tiles
	
	@Override
	public int getTileConnectionCount(final Integer tileId) {
		return(tiles.getConnectionCount(Requires.notNull(tileId, "tileId == null")));
	}
	
	@Override
	public boolean isTileConnected(final Integer tileId, final int connectionId) {
		return(tiles.isTileConnected(Requires.notNull(tileId, "tileId == null"), Requires.positive(connectionId, "connectionId < 0")));
	}
	
	@Override
	public int getConnectedTile(final Integer tileId, final int connectionId) {
		return(tiles.getConnectedTile(Requires.notNull(tileId, "tileId == null"), Requires.notNull(connectionId, "connectionId == null")));
	}
	
	@Override
	public int getTileType(final Integer tileId) {
		return(tiles.getTileType(tileId));
	}
	
	@Override
	public TileState getTileState(final Integer tileId) {
		final TileState state = tileStates.get(Requires.notNull(tileId, "tileId == null"));
		return(state != null ? state : TileState.NEVER_SEEN);
	}
	
	@Override
	public TileState setTileState(final Integer tileId, final TileState state) {
		return(tileStates.put(Requires.notNull(tileId, "tileId == null"), Requires.notNull(state, "state == null")));
	}
	
	@Override
	public TileState removeTileState(final Integer tileId) {
		return(tileStates.remove(Requires.notNull(tileId, "tileId == null")));
	}
	
	@Override
	public void clearTileStates() {
		tileStates.clear();
	}
	
	// Unit
	
	@Override
	public Unit addUnitToTile(final Integer tileId, final Unit unit) {
		final Unit oldUnit = tileIdToUnitMap.put(Requires.notNull(tileId, "tileId == null"), Requires.notNull(unit, "unit == null"));
		return(oldUnit);
	}
	
	@Override
	public boolean isUnitOnTile(final Integer tileId) {
		return(tileIdToUnitMap.containsKey(Requires.notNull(tileId, "tileId == null")));
	}
	
	@Override
	public Unit getUnitOnTile(final Integer tileId) {
		return(tileIdToUnitMap.get(Requires.notNull(tileId, "tileId == null")));
	}
	
	@Override
	public Unit removeUnitFromTile(final Integer tileId) {
		final Unit unit = tileIdToUnitMap.remove(Requires.notNull(tileId, "tileId == null"));
		return(unit);
	}
	
	@Override
	public void clearUnits() {
		tileIdToUnitMap.clear();
	}
	
	
	@Override
	public int getCurrentPlayerId() {
		return(currentPlayerId);
	}
	
	// Player
	
	@Override
	public int getPlayerCount() {
		return (players.length);
	}
	
	@Override
	public Player getPlayer(final int i) {
		return (players[i]);
	}
	
	public int getMaxPlayerCount() {
		return (playerCount);
	}
	
	// Misc
	
	@Override
	public Weather getWeather() {
		return(currentWeather);
	}
	
	public void setWeather(final Weather currentWeather) {
		this.currentWeather = Requires.notNull(currentWeather, "currentWeather == null");
	}
	
	// Business
	
	@Override
	public void endTurn(final DataSink<? super UnitEndEvent> callback) {
		clearUnits();
		clearTileStates();
		
		final int playerCount = players.length;
		for(currentPlayerId = (currentPlayerId + 1) % playerCount;
				!players[currentPlayerId].isEnabled();
				currentPlayerId = (currentPlayerId + 1) % playerCount)
			;
		
		callback.sink(new UnitEndEvent(currentPlayerId));
	}
}
