package online.money_daisuki.gaming.tbs.models.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;

public final class DataModelLoader {
	private final JsonMap map;
	
	public DataModelLoader(final JsonMap map) {
		this.map = Requires.notNull(map, "map == null");
	}
	public DataModel source() {
		final Drive[] drives = loadDrives();
		final Weather[] weather = loadWeathers();
		final TileConnection[] tileConnections = loadTileConnections();
		final TileTemplate[] tiles = loadTileTemplates(drives);
		final WeatherTile[] weatherTiles = loadWeatherTiles(weather, tiles);
		final Ammo[] ammos = loadAmmoTypes();
		final Weapon[] weapons = loadWeapons(ammos, drives);
		final UnitTemplate[] units = loadUnits(weapons, drives, tileConnections.length, 1);
		
		return(new DataModelImpl(drives, weather, tileConnections, tiles, weatherTiles, units));
	}
	private Drive[] loadDrives() {
		final JsonList drivesList = map.get("drives").asList();
		
		
		final int size = drivesList.size();
		final Drive[] drives = new Drive[size];
		
		for(int i = 0; i < size; i++) {
			drives[i] = loadDrive(drivesList.get(i).asMap());
		}
		return(drives);
	}
	private Drive loadDrive(final JsonMap map) {
		final String name = map.get("name").asData().asString();
		final boolean terrainDefenseApplicable = map.get("terrainDefenseApplicable").asData().asBool();
		final int fuelLossPerRound = map.get("fuelLossPerRound").asData().asNumber().asBigInteger().intValueExact();
		
		return(new DriveImpl(name, terrainDefenseApplicable, fuelLossPerRound));
	}
	private Weather[] loadWeathers() {
		final JsonList weatherList = map.get("weathers").asList();
		final int size = weatherList.size();
		final Weather[] weathers = new Weather[size];
		
		for(int i = 0; i < size; i++) {
			weathers[i] = new WeatherImpl(weatherList.get(i).asMap().get("name").asData().asString());
		}
		return(weathers);
	}
	private TileConnection[] loadTileConnections() {
		final JsonList connectionList = map.get("tileConnections").asList();
		final int size = connectionList.size();
		final TileConnection[] connections = new TileConnection[size];
		
		for(int i = 0; i < size; i++) {
			final JsonMap m = connectionList.get(i).asMap();
			
			final String has = m.get("has").asData().asString();
			final String id = m.get("id").asData().asString();
			
			connections[i] = new TileConnectionImpl(has, id);
		}
		return(connections);
	}
	private TileTemplate[] loadTileTemplates(final Drive[] drives) {
		final JsonList tileList = map.get("tiles").asList();
		final int size = tileList.size();
		final TileTemplate[] tiles = new TileTemplate[size];
		
		for(int i = 0; i < size; i++) {
			tiles[i] = loadTileTemplate(drives, tileList.get(i).asMap());
		}
		return(tiles);
	}
	private TileTemplate loadTileTemplate(final Drive[] drives, final JsonMap map) {
		final String name = map.get("name").asData().asString();
		final Map<Drive, Integer> enterCosts = map.containsKey("enterCost") ?
				loadObjectToIntMap(drives, map.get("enterCost").asMap()) :
					new HashMap<>();
		final Map<Drive, Integer> exitCosts = map.containsKey("exitCost") ?
				loadObjectToIntMap(drives, map.get("exitCost").asMap()) :
					new HashMap<>();
		
		final JsonMap imagesMap = map.get("images").asMap();
		final String visibleImageUrl = imagesMap.get("visible").asData().asString();
		final String invisibleImageUrl = imagesMap.get("invisible").asData().asString();
		final String neverseenImageUrl = imagesMap.get("neverseen").asData().asString();
		
		final int defensive = (int) map.get("defensive").asData().asNumber().asInt();
		
		return(new TileTemplateImpl(name, defensive, enterCosts, exitCosts, visibleImageUrl,
				invisibleImageUrl, neverseenImageUrl));
	}
	private WeatherTile[] loadWeatherTiles(final Weather[] weathers, final TileTemplate[] tileTemplates) {
		final JsonList tileList = map.get("weatherTiles").asList();
		final int size = tileList.size();
		final WeatherTile[] tiles = new WeatherTile[size];
		
		for(int i = 0; i < size; i++) {
			tiles[i] = loadWeatherTile(weathers, tileTemplates, tileList.get(i).asMap());
		}
		return(tiles);
	}
	private WeatherTile loadWeatherTile(final Weather[] weathers, final TileTemplate[] tileTemplates, final JsonMap map) {
		final String name = map.get("name").asData().asString();
		final Map<Weather, TileTemplate> tiles = loadObjectToObjectMap(weathers, tileTemplates, map.get("tiles").asMap());
		return(new WeatherTileImpl(name, tiles));
	}
	private Ammo[] loadAmmoTypes() {
		final JsonList ammoList = map.get("ammos").asList();
		final int size = ammoList.size();
		final Ammo[] ammos = new Ammo[size];
		
		for(int i = 0; i < size; i++) {
			final String name = ammoList.get(i).asMap().get("name").asData().asString();
			final AmmoType type = AmmoType.valueOf(ammoList.get(i).asMap().get("type").asData().asString());
			ammos[i] = new AmmoImpl(name, type);
		}
		return(ammos);
	}
	private Weapon[] loadWeapons(final Ammo[] ammos, final Drive[] drives) {
		final JsonList weaponList = map.get("weapons").asList();
		final int size = weaponList.size();
		final Weapon[] weapons = new Weapon[size];
		
		for(int i = 0; i < size; i++) {
			final JsonMap weaponMap = weaponList.get(i).asMap();
			
			final String name = weaponMap.get("name").asData().asString();
			final Ammo ammo = ammos[weaponMap.get("ammo").asData().asNumber().asBigInteger().intValueExact()];
			final int maxAmmo = weaponMap.get("maxAmmo").asData().asNumber().asBigInteger().intValueExact();
			final int minDistance = weaponMap.get("minDistance").asData().asNumber().asBigInteger().intValueExact();
			final int maxDistance = weaponMap.get("maxDistance").asData().asNumber().asBigInteger().intValueExact();
			final int strength = weaponMap.get("strength").asData().asNumber().asBigInteger().intValueExact();
			
			final JsonList canAttackList = weaponMap.get("canAttack").asList();
			final Set<Drive> canAttack = new HashSet<>();
			for(final JsonElement e:canAttackList) {
				canAttack.add(drives[(int) e.asData().asNumber().asInt()]);
			}
			final boolean canGetCounterstriked = weaponMap.get("canGetCounterstriked").asData().asBool();
			
			weapons[i] = new WeaponImpl(name, ammo, maxAmmo, minDistance, maxDistance, strength, canAttack,
					canGetCounterstriked);
		}
		return(weapons);
	}
	private UnitTemplate[] loadUnits(final Weapon[] weapons, final Drive[] drives, final int tileConnectionCount,
			final int maxPlayerCount) {
		final JsonList weaponList = map.get("units").asList();
		final int size = weaponList.size();
		final UnitTemplate[] units = new UnitTemplate[size];
		
		for(int i = 0; i < size; i++) {
			units[i] = loadUnits(weapons, drives, tileConnectionCount, maxPlayerCount,
					weaponList.get(i).asMap());
		}
		return(units);
	}
	private UnitTemplate loadUnits(final Weapon[] weapons, final Drive[] drives, final int tileConnectionCount,
			final int maxPlayerCount, final JsonMap map) {
		final String name = map.get("name").asData().asString();
		
		final JsonList weaponsList = map.get("weapons").asList();
		final Weapon[] unitWeapons = new Weapon[weaponsList.size()];
		for(int i = 0, size = weaponsList.size(); i < size; i++) {
			unitWeapons[i] = weapons[(int) weaponsList.get(i).asData().asNumber().asInt()];
		}
		
		final int defense = (int) map.get("defense").asData().asNumber().asInt();
		final int moveDistance = (int) map.get("move").asData().asNumber().asInt();
		final Drive drive = drives[(int) map.get("drive").asData().asNumber().asInt()];
		
		final int maxFuel = (int) map.get("maxFuel").asData().asNumber().asInt();
		final int sight = (int) map.get("sight").asData().asNumber().asInt();
		final int weight = (int) map.get("weight").asData().asNumber().asInt();
		final int cargo = (int) map.get("cargo").asData().asNumber().asInt();
		
		final boolean moveXorAttack = map.get("moveXorAttack").asData().asBool();
		final boolean canCapturing = map.get("canCapturing").asData().asBool();
		final boolean driveByShooting = map.get("driveByShooting").asData().asBool();
		
		final JsonList imagesList = map.get("images").asList();
		
		final String[][] imageUrls = new String[tileConnectionCount][2]; //TODO
		for(int i = 0; i < tileConnectionCount; i++) {
			final JsonList playerImagesList = imagesList.get(i).asList();
			for(int j = 0; j < 2; j++) {
				imageUrls[i][j] = playerImagesList.get(j).asData().asString();
			}
		}
		
		return(new UnitTemplateImpl(name, unitWeapons, defense, moveDistance, drive, moveXorAttack, canCapturing,
				driveByShooting, maxFuel, sight, weight, cargo, imageUrls));
	}
	
	private <T> Map<T, Integer> loadObjectToIntMap(final T[] arr, final JsonMap map) {
		final Map<T, Integer> m = new HashMap<>();
		
		for(final Entry<String, JsonElement> e:map.entrySet()) {
			final int id = Integer.parseInt(e.getKey());
			final int cost = (int) e.getValue().asData().asNumber().asInt();
			m.put(arr[id], cost);
		}
		return(m);
	}
	private <T, U> Map<T, U> loadObjectToObjectMap(final T[] arr0, final U[] arr1, final JsonMap map) {
		final Map<T, U> m = new HashMap<>();
		
		for(final Entry<String, JsonElement> e:map.entrySet()) {
			final int id0 = Integer.parseInt(e.getKey());
			final int id1 = (int) e.getValue().asData().asNumber().asInt();
			m.put(arr0[id0], arr1[id1]);
		}
		return(m);
	}
}
