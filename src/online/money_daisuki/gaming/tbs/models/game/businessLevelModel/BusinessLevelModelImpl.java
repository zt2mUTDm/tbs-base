package online.money_daisuki.gaming.tbs.models.game.businessLevelModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import online.money_daisuki.api.algorithms.graph.pathfinding.shortest.bruteforce.GraphMovecostCalculator;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.NullDataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModelImpl;
import online.money_daisuki.api.utils.CollectDataSink;
import online.money_daisuki.api.utils.CollectDataSinkImpl;
import online.money_daisuki.gaming.tbs.models.data.DataModel;
import online.money_daisuki.gaming.tbs.models.data.Drive;
import online.money_daisuki.gaming.tbs.models.data.TileTemplate;
import online.money_daisuki.gaming.tbs.models.data.UnitTemplate;
import online.money_daisuki.gaming.tbs.models.data.Weapon;
import online.money_daisuki.gaming.tbs.models.data.Weather;
import online.money_daisuki.gaming.tbs.models.game.FailedUnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.FogOfWarModel;
import online.money_daisuki.gaming.tbs.models.game.FogOfWarModelImpl;
import online.money_daisuki.gaming.tbs.models.game.Player;
import online.money_daisuki.gaming.tbs.models.game.TileField;
import online.money_daisuki.gaming.tbs.models.game.TileGraphViewCalculatorModel;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;
import online.money_daisuki.gaming.tbs.models.game.Unit;
import online.money_daisuki.gaming.tbs.models.game.UnitAttackedEvent;
import online.money_daisuki.gaming.tbs.models.game.UnitEndEvent;
import online.money_daisuki.gaming.tbs.models.game.UnitMovedEvent;
import online.money_daisuki.gaming.tbs.models.game.UnitMovedEventImpl;
import online.money_daisuki.gaming.tbs.models.game.UpdateGameDataEvent;

public final class BusinessLevelModelImpl implements LocalBusinessLevelModel {
	private final TileField tiles;
	private final Set<Integer> tileIds;
	private final Map<Integer, Unit> tileIdToUnitMap;
	private final Player[] players;
	private final Map<Player, FogOfWarModel> fogOfWarModels;
	private final DataModel data;
	
	private final Weather currentWeather;
	
	private int currentPlayerId;
	private FogOfWarModel currentFogModel;
	
	public BusinessLevelModelImpl(final TileField tiles, final Map<Integer, Unit> tileIdToUnitMap,
			 final Weather currentWeather, final Player[] players, final Map<Player, FogOfWarModelImpl> fogOfWarModels,
			 final DataModel data) {
		this.tiles = Requires.notNull(tiles, "tiles == null");
		this.tileIdToUnitMap = Requires.containsNotNull(new HashMap<Integer, Unit>(Requires.containsNotNull(tileIdToUnitMap, "tileIdToUnitMap == null")));
		this.currentWeather = Requires.notNull(currentWeather, "currentWeather == null");
		this.players = Requires.containsNotNull(Arrays.copyOf(players, Requires.notNull(players, "players == null").length), "players contains null");
		this.fogOfWarModels = Requires.notNull(new HashMap<>(Requires.notNull(fogOfWarModels, "fogOfWarModels")), "fogOfWarModels contains null");
		// TODO Check model for each player and change to array
		this.currentFogModel = fogOfWarModels.get(players[0]);
		this.data = Requires.notNull(data, "data == null");
		
		this.tileIds = new HashSet<>();
		for(int i = 0, size = tiles.getTileCount(); i < size; i++) {
			tileIds.add(i);
		}
		
		initialFogOfWar();
	}	
	private void initialFogOfWar() {
		for(final Entry<Integer, Unit> e:tileIdToUnitMap.entrySet()) {
			final Integer tileId = e.getKey();
			final Unit unit = e.getValue();
			
			final FogOfWarModel model = fogOfWarModels.get(players[unit.getPlayerId()]);
			addVisibility(tileId, unit.getTemplate(), model, new NullDataSink<>());
		}
	}
	private void addVisibility(final Integer startTileId, final UnitTemplate unitTemp, final FogOfWarModel model,
			final DataSink<Integer> changedTiles) {
		 final Map<Integer, Integer> costs = calculateVisibility(startTileId, unitTemp);
		for(final Entry<Integer, Integer> e:costs.entrySet()) {
			final Integer tileId = e.getKey();
			final Integer viewStretch = e.getValue();
			
			final TileState oldState = model.getTileState(tileId);
			model.addVisibility(tileId, unitTemp, viewStretch);
			final TileState newState = model.getTileState(tileId);
			
			if(oldState != newState) {
				changedTiles.sink(tileId);
			}
		}
	}
	private void removeVisibility(final Integer startTileId, final UnitTemplate unitTemp,
			final FogOfWarModel model, final DataSink<Integer> changedTiles) {
		 final Map<Integer, Integer> costs = calculateVisibility(startTileId, unitTemp);
		for(final Entry<Integer, Integer> e:costs.entrySet()) {
			final Integer tileId = e.getKey();
			final Integer viewStretch = e.getValue();
			
			final TileState oldState = model.getTileState(tileId);
			model.removeVisibility(tileId, unitTemp, viewStretch);
			final TileState newState = model.getTileState(tileId);
			
			if(oldState != newState) {
				changedTiles.sink(tileId);
			}
		}
	}
	private Map<Integer, Integer> calculateVisibility(final Integer tileId, final UnitTemplate unitTemp) {
		return(new GraphMovecostCalculator<>(
				new TileGraphViewCalculatorModel(tileId, unitTemp, new TileGraphViewCalculatorSubModel() {
					@Override
					public boolean isTileConnected(final Integer tileId, final int i) {
						return(tiles.isTileConnected(tileId, i));
					}
					@Override
					public int getTileConnectionCount(final Integer tileId) {
						return(tiles.getConnectionCount(tileId));
					}
					@Override
					public Integer getConnectedTile(final Integer tileId, final int i) {
						return(tiles.getConnectedTile(tileId, i));
					}
				})
		).source());
	}
	
	private Unit getUnitOnTile(final Integer tileId) {
		return(tileIdToUnitMap.get(tileId));
	}
	private TileState getTileState(final Integer tileId) {
		return(currentFogModel.getTileState(tileId));
	}
	
	@Override
	public void moveUnit(final Deque<Integer> tiles, final DataSink<? super UnitMovedEvent> callback) {
		moveUnit0(new LinkedList<>(tiles), callback);
	}
	private void moveUnit0(final Deque<Integer> tiles, final DataSink<? super UnitMovedEvent> callback) {
		final Integer startTileId = tiles.removeFirst();
		Integer actualTileId = startTileId;
		final Unit unit = tileIdToUnitMap.get(actualTileId);
		if(unit == null || unit.getPlayerId() != currentPlayerId) {
			callback.sink(new FailedUnitMovedEvent());
			return;
		}
		
		final int initUnitConnection = unit.getViewDirection();
		
		final SetableMutableSingleValueModelImpl<UnitAttackedEvent> attackEvent = new SetableMutableSingleValueModelImpl<>();
		
		int moveCosts = 0;
		int lastExitCosts = 0;
		
		Integer nextTileId = actualTileId;
		while(!tiles.isEmpty()) {
			actualTileId = nextTileId;
			nextTileId = tiles.removeFirst();
			
			final int connection = searchConnectedTile(actualTileId, nextTileId);
			if(connection == -1) {
				unit.setViewDirection(initUnitConnection);
				callback.sink(new FailedUnitMovedEvent());
				return;
			}
			
			final int tileType = this.tiles.getTileType(nextTileId);
			final TileTemplate tileTemp = data.getWeatherTile(tileType).getTile(currentWeather);
			
			moveCosts+= tileTemp.getEnterCosts(unit.getTemplate().getDrive());
			moveCosts+= lastExitCosts;
			lastExitCosts = tileTemp.getExitCosts(unit.getTemplate().getDrive());
			if(moveCosts < 0) {
				unit.setViewDirection(initUnitConnection);
				callback.sink(new FailedUnitMovedEvent());
				return;
			}
			
			final TileState state = getTileState(nextTileId);
			if(!state.canMoveOn()) {
				unit.setViewDirection(initUnitConnection);
				callback.sink(new FailedUnitMovedEvent());
				return;
			}
			
			final Unit fieldUnit = tileIdToUnitMap.get(nextTileId);
			if(fieldUnit != null) {
				if(fieldUnit.getPlayerId() != unit.getPlayerId()) {
					if(state.seeUnits()) {
						unit.setViewDirection(initUnitConnection);
						callback.sink(new FailedUnitMovedEvent());
						return;
					} else {
						final UnitAttackedEvent e = fightIfPossible(startTileId, nextTileId, 1);
						if(e != null) {
							attackEvent.sink(new UnitAttackedEvent(actualTileId, nextTileId, e.getAttackHp(),
									e.getDefenseHp()));
						}
						nextTileId = actualTileId;
						break;
					}
				}
			}
			unit.setViewDirection(connection);
		}
		
		if(moveCosts > Math.min(unit.getFuel(), unit.getTemplate().getMoveDistance())) {
			unit.setViewDirection(initUnitConnection);
			callback.sink(new FailedUnitMovedEvent());
			return;
		}
		
		final Unit fieldUnit = tileIdToUnitMap.get(nextTileId);
		if(fieldUnit != null) {
			unit.setViewDirection(initUnitConnection);
			callback.sink(new FailedUnitMovedEvent());
			return;
		}
		
		final CollectDataSink<Integer> removeCollectSink = new CollectDataSinkImpl<>();
		final CollectDataSink<Integer> addCollectSink = new CollectDataSinkImpl<>();
		
		removeVisibility(startTileId, unit.getTemplate(), currentFogModel, removeCollectSink);
		addVisibility(nextTileId, unit.getTemplate(), currentFogModel, addCollectSink);
		
		final Collection<Integer> removeCollection = new HashSet<>(removeCollectSink.source());
		final Collection<Integer> addCollection = new HashSet<>(addCollectSink.source());
		addCollection.removeAll(removeCollection);
		
		final Map<Integer, Unit> newUnits = new HashMap<>();
		for(final Integer tileId:addCollection) {
			final TileState state = getTileState(tileId);
			if(state.seeUnits()) {
				final Unit newUnit = getUnitOnTile(tileId);
				if(newUnit != null) {
					newUnits.put(tileId, newUnit);
				}
			}
		}
		
		removeCollection.removeAll(addCollection);
		
		final HashMap<Integer, TileState> newTileStates = new HashMap<>();
		for(final Integer i:addCollection) {
			newTileStates.put(i, getTileState(i));
		}
		for(final Integer i:removeCollection) {
			newTileStates.put(i, getTileState(i));
		}
		
		tileIdToUnitMap.remove(startTileId);
		tileIdToUnitMap.put(nextTileId, unit);
		
		unit.move(moveCosts);
		
		callback.sink(new UnitMovedEventImpl(true, nextTileId, newTileStates, newUnits, attackEvent));
	}
	
	private UnitAttackedEvent fightIfPossible(final Integer attTile, final Integer defTile, final int distance) {
		final Unit attUnit = getUnitOnTile(attTile);
		final Unit defUnit = getUnitOnTile(defTile);
		
		final Weapon attWeapon = getFightWeapon(attUnit, defUnit, distance);
		final Weapon defWeapon = getFightWeapon(defUnit, attUnit, 1);
		
		if(attWeapon == null && defWeapon == null) {
			return(null);
		}
		
		final TileTemplate attTileTemp = data.getWeatherTile(this.tiles.getTileType(attTile)).getTile(currentWeather);
		final TileTemplate defTileTemp = data.getWeatherTile(this.tiles.getTileType(defTile)).getTile(currentWeather);
		
		final UnitTemplate attUnitTemp = attUnit.getTemplate();
		final UnitTemplate defUnitTemp = defUnit.getTemplate();
		
		final int attDef = attUnitTemp.getDefense() + (attUnitTemp.getDrive().terrainDefenseApplicable() ? attTileTemp.getDefensive() : 0);
		final int defDef = defUnitTemp.getDefense() + (defUnitTemp.getDrive().terrainDefenseApplicable() ? defTileTemp.getDefensive() : 0);
		
		final int attWeaponDmg = attWeapon != null ? attWeapon.getStrength() : 0;
		final int attAtt = (int) (attWeaponDmg + (attWeaponDmg * 0.1 * attUnit.getLevel()));
		
		final int defWeaponDmg = defWeapon != null ? defWeapon.getStrength() : 0;
		final int defAtt = (int) (defWeaponDmg + (defWeaponDmg * 0.1 * defUnit.getLevel()));
		
		final int attDmg = Math.max(defAtt - attDef, 0);
		final int defDmg = Math.max(attAtt - defDef, 0);
		
		final int attHp = attUnit.getHp();
		final int newAttHp = Math.max(attHp - attDmg, 0);
		if(newAttHp == 0) {
			tileIdToUnitMap.remove(attTile);
			removeVisibility(attTile, attUnitTemp, fogOfWarModels.get(players[attUnit.getPlayerId()]),
					new NullDataSink<>());
			defUnit.levelUp();
		} else {
			attUnit.setHp(newAttHp);
			attUnit.attack();
			attUnit.levelUp();
		}
		
		final int defHp = defUnit.getHp();
		final int newDefHp = Math.max(defHp - defDmg, 0);
		if(newDefHp == 0) {
			tileIdToUnitMap.remove(defTile);
			removeVisibility(defTile, defUnitTemp, fogOfWarModels.get(players[defUnit.getPlayerId()]),
					new NullDataSink<>());
			attUnit.levelUp();
		} else {
			defUnit.setHp(newDefHp);
			defUnit.levelUp();
		}
		
		return(new UnitAttackedEvent(attTile, defTile, newAttHp, newDefHp));
	}
	@Override
	public void attackUnit(final Deque<Integer> tiles, final DataSink<? super UnitAttackedEvent> callback) {
		attackUnit0(new LinkedList<>(tiles), callback);
	}
	private void attackUnit0(final Deque<Integer> tiles, final DataSink<? super UnitAttackedEvent> callback) {
		if(tiles.size() <= 1) {
			callback.sink(UnitAttackedEvent.createFailure());
			return;
		}
		
		final Integer attTile = tiles.removeFirst();
		final boolean meele = tiles.size() == 1;
		
		final Unit attUnit = tileIdToUnitMap.get(attTile);
		if(attUnit == null) {
			callback.sink(UnitAttackedEvent.createFailure());
			return;
		}
		
		final int tileCount = tiles.size();
		final Weapon attWeapon = attUnit.getTemplate().getWeapon(0); // TODO
		if(tileCount < attWeapon.getMinDistance() || tileCount > attWeapon.getMaxDistance()) {
			callback.sink(UnitAttackedEvent.createFailure());
			return;
		}
		
		Integer actualTile;
		Integer nextTile = attTile;
		while(!tiles.isEmpty()) {
			actualTile = nextTile;
			nextTile = tiles.removeFirst();
			if(searchConnectedTile(actualTile, nextTile) == -1) {
				callback.sink(UnitAttackedEvent.createFailure());
				return;
			}
		}
		
		final Unit defUnit = tileIdToUnitMap.get(nextTile);
		if(attUnit.getPlayerId() == defUnit.getPlayerId()) {
			callback.sink(UnitAttackedEvent.createFailure());
			return;
		}
		
		final UnitTemplate attUnitTemp = attUnit.getTemplate();
		final UnitTemplate defUnitTemp = defUnit.getTemplate();
		
		if(!attWeapon.canAttack(defUnitTemp.getDrive())) {
			callback.sink(UnitAttackedEvent.createFailure());
			return;
		}
		
		final TileTemplate attTileTemp = data.getWeatherTile(this.tiles.getTileType(attTile)).getTile(currentWeather);
		final TileTemplate defTileTemp = data.getWeatherTile(this.tiles.getTileType(nextTile)).getTile(currentWeather);
		
		final int attWeaponDmg = attWeapon.getStrength();
		final int attAtt = (int) (attWeaponDmg + (attWeaponDmg * 0.1 * attUnit.getLevel()));
		final int defAtt;
		if(meele && attWeapon.canGetCounterstriked() && defUnitTemp.getWeaponCount() > 0) {
			final Weapon defWeapon = getFightWeapon(defUnit, attUnit, 1);
			if(defWeapon != null) {
				final int defWeaponDmg = defWeapon.getStrength();
				defAtt = (int) (defWeaponDmg + (defWeaponDmg * 0.1 * defUnit.getLevel()));
			} else {
				defAtt = 0;
			}
		} else {
			defAtt = 0;
		}
		
		final int attDef = attUnitTemp.getDefense() + (attUnitTemp.getDrive().terrainDefenseApplicable() ? attTileTemp.getDefensive() : 0);
		final int defDef = defUnitTemp.getDefense() + (defUnitTemp.getDrive().terrainDefenseApplicable() ? defTileTemp.getDefensive() : 0);
		
		final int attDmg = Math.max(defAtt - attDef, 0);
		final int defDmg = Math.max(attAtt - defDef, 0);
		
		final int attHp = attUnit.getHp();
		final int newAttHp = Math.max(attHp - attDmg, 0);
		if(newAttHp == 0) {
			tileIdToUnitMap.remove(attTile);
			removeVisibility(attTile, attUnitTemp, fogOfWarModels.get(players[attUnit.getPlayerId()]),
					new NullDataSink<>());
			defUnit.levelUp();
		} else {
			attUnit.setHp(newAttHp);
			attUnit.attack();
			attUnit.levelUp();
		}
		
		final int defHp = defUnit.getHp();
		final int newDefHp = Math.max(defHp - defDmg, 0);
		if(newDefHp == 0) {
			tileIdToUnitMap.remove(nextTile);
			removeVisibility(nextTile, defUnitTemp, fogOfWarModels.get(players[defUnit.getPlayerId()]),
					new NullDataSink<>());
			attUnit.levelUp();
		} else {
			defUnit.setHp(newDefHp);
			defUnit.levelUp();
		}
		
		callback.sink(new UnitAttackedEvent(attTile, nextTile, newAttHp, newDefHp));
	}
	private Weapon getFightWeapon(final Unit att, final Unit def, final int distance) {
		final UnitTemplate attUnitTemplate = att.getTemplate();
		final Drive defUnitDrive = def.getTemplate().getDrive();
		
		Weapon best = null;
		for(int i = 0, size = attUnitTemplate.getWeaponCount(); i < size; i++) {
			final Weapon w = attUnitTemplate.getWeapon(i);
			if(w.canAttack(defUnitDrive) && w.getMinDistance() >= distance && w.getMaxDistance() <= distance) {
				best = (best == null || best.getStrength() < w.getStrength() ? w : best);
			}
		}
		return(best);
	}
	
	@Override
	public void endTurn(final DataSink<? super UnitEndEvent> callback) {
		for(final Entry<Integer, Unit> e:tileIdToUnitMap.entrySet()) {
			final Unit u = e.getValue();
			if(u.getPlayerId() == currentPlayerId) {
				u.reset();
			}
		}
		
		for(currentPlayerId = (currentPlayerId + 1) % players.length;
				!players[currentPlayerId].isEnabled();
				currentPlayerId = (currentPlayerId + 1) % players.length)
			;
		currentFogModel = fogOfWarModels.get(players[currentPlayerId]);
		
		callback.sink(new UnitEndEvent(currentPlayerId));
	}
	
	private int searchConnectedTile(final Integer actualTileId, final Integer nextTile) {
		final int at = actualTileId.intValue();
		final int nt = nextTile.intValue();
		
		for(int i = 0, size = tiles.getConnectionCount(at); i < size; i++) {
			if(tiles.isTileConnected(at, i)) {
				if(tiles.getConnectedTile(at, i) == nt) {
					return(i);
				}
			}
		}
		return(-1);
	}
	
	@Override
	public void getGameData(final DataSink<? super UpdateGameDataEvent> callback) {
		final Map<Integer, TileState> tileStates = new HashMap<>();
		final Map<Integer, Unit> visibleUnits = new HashMap<>();
		
		for(final Integer tileId:tileIds) {
			final TileState state = getTileState(tileId);
			
			tileStates.put(tileId, state);
			if(state.seeUnits()) {
				final Unit unit = getUnitOnTile(tileId);
				if(unit != null) {
					visibleUnits.put(tileId, unit);
				}
			}
		}
		
		callback.sink(new UpdateGameDataEvent(tileStates, visibleUnits));
	}
	
	@Override
	public int getCurrentPlayerId() {
		return(currentPlayerId);
	}
}
