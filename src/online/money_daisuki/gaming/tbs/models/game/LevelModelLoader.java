package online.money_daisuki.gaming.tbs.models.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.gaming.tbs.models.data.DataModel;
import online.money_daisuki.gaming.tbs.models.data.UnitTemplate;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;

public final class LevelModelLoader implements DataSource<LevelModel> {
	private final JsonMap map;
	private final Player[] players;
	private final DataModel data;
	
	public LevelModelLoader(final JsonMap map, final Player[] players, final DataModel data) {
		this.map = Requires.notNull(map, "map == null");
		this.players = Requires.containsNotNull(Arrays.copyOf(players, Requires.notNull(players, "players == null").length), "players contains null");
		this.data = Requires.notNull(data, "data == null");
	}
	@Override
	public LevelModel source() {
		final int maxPlayerCount = (int) map.get("maxPlayerCount").asData().asNumber().asInt();
		
		final TileField tiles = new GenericJsonTileFieldLoader(map.get("tiles").asMap().get("model").asList(), 4).source();
		
		final Map<Integer, Unit> tileIdToUnitMap = createUnits(map.get("units").asList());
		
		final Map<Player, FogOfWarModelImpl> fow = new HashMap<>();
		for(final Player p:players) {
			fow.put(p, new FogOfWarModelImpl(new Converter<Integer, TileState>() {
				@Override
				public TileState convert(final Integer value) {
					return(value >= 1 ? TileState.VISIBLE : TileState.INVISIBLE);
				}
			}));
		}
		
		return(new LevelModelImpl(maxPlayerCount, tiles, tileIdToUnitMap, data.getWeather(0), players, fow));
	}
	private Map<Integer, Unit> createUnits(final JsonList unitsList) {
		final Map<Integer, Unit> tileIdToUnitMap = new HashMap<>();
		for(final JsonElement e:unitsList) {
			createUnitInstance(e.asMap(), tileIdToUnitMap);
		}
		return(tileIdToUnitMap);
	}
	private void createUnitInstance(final JsonMap unitMap, final Map<Integer, Unit> tileIdToUnitMap) {
		final int unitId = (int) unitMap.get("id").asData().asNumber().asInt();
		final UnitTemplate temp = data.getUnitTemplate(unitId);
		
		final int playerId = (int) unitMap.get("player").asData().asNumber().asInt();
		final int tile = (int) unitMap.get("tile").asData().asNumber().asInt();
		
		final int hp = (int) unitMap.get("hp").asData().asNumber().asInt();
		final int fuel = unitMap.containsKey("fuel") ? (int) unitMap.get("hp").asData().asNumber().asInt() : temp.getMaxFuel();
		
		final Unit unit = new UnitImpl(temp, playerId, hp, fuel);
		
		tileIdToUnitMap.put(tile, unit);
	}
}
