package online.money_daisuki.gaming.tbs.models.game.businessLevelModel;

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
import online.money_daisuki.gaming.tbs.models.game.FogOfWarModelImpl;
import online.money_daisuki.gaming.tbs.models.game.GenericJsonTileFieldLoader;
import online.money_daisuki.gaming.tbs.models.game.Player;
import online.money_daisuki.gaming.tbs.models.game.TileField;
import online.money_daisuki.gaming.tbs.models.game.TileStateModel.TileState;
import online.money_daisuki.gaming.tbs.models.game.Unit;
import online.money_daisuki.gaming.tbs.models.game.UnitImpl;

public final class BusinessLevelModelLoader implements DataSource<BusinessLevelModel> {
	private final JsonMap map;
	private final Player[] players;
	private final DataModel data;
	
	public BusinessLevelModelLoader(final JsonMap map, final Player[] players, final DataModel data) {
		this.map = Requires.notNull(map, "map == null");
		this.players = Requires.containsNotNull(Arrays.copyOf(players, Requires.notNull(players, "players == null").length), "players contains null");
		this.data = Requires.notNull(data, "data == null");
	}
	@Override
	public BusinessLevelModel source() {
		//final Map<TileTemplate, TileImages> tileImages = loadTileImages();
		//final Map<UnitTemplate, BufferedImage[][]> unitImages = loadUnitImages();
		
		final TileField tiles = new GenericJsonTileFieldLoader(map.get("tiles").asList(), 4).source();
		
		final Map<Integer, Unit> tileIdToUnitMap = createUnits(map.get("units").asList(), players);
		
		final Map<Player, FogOfWarModelImpl> fow = new HashMap<>();
		for(final Player p:players) {
			fow.put(p, new FogOfWarModelImpl(new Converter<Integer, TileState>() {
				@Override
				public TileState convert(final Integer value) {
					return(value >= 1 ? TileState.VISIBLE : TileState.INVISIBLE);
				}
			}));
		}
		
		return(new BusinessLevelModelImpl(tiles, tileIdToUnitMap, data.getWeather(0), players, fow, data));
	}
	private Map<Integer, Unit> createUnits(final JsonList unitsList, final Player[] players) {
		final Map<Integer, Unit> tileIdToUnitMap = new HashMap<>();
		for(final JsonElement e:unitsList) {
			final JsonMap unitMap = e.asMap();
			
			final int unitId = (int) unitMap.get("id").asData().asNumber().asInt();
			final int playerId = (int) unitMap.get("player").asData().asNumber().asInt();
			if(!players[playerId].isEnabled()) {
				continue;
			}
			
			final int hp = (int) unitMap.get("hp").asData().asNumber().asInt();
			final int tile = (int) unitMap.get("tile").asData().asNumber().asInt();
			
			final UnitTemplate temp = data.getUnitTemplate(unitId);
			
			final Unit unit = new UnitImpl(temp, playerId, hp);
			
			tileIdToUnitMap.put(tile, unit);
		}
		return(tileIdToUnitMap);
	}
}
