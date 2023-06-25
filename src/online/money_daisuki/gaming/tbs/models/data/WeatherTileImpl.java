package online.money_daisuki.gaming.tbs.models.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class WeatherTileImpl implements WeatherTile {
	private final String name;
	private final Map<Weather, TileTemplate> tiles;
	
	public WeatherTileImpl(final String name, final Map<Weather, TileTemplate> tiles) {
		this.name = Requires.notNull(name, "name == null");
		this.tiles = Requires.containsNotNull(new HashMap<>(Requires.notNull(tiles, "tiles == null")));
	}
	@Override
	public String getName() {
		return (name);
	}
	@Override
	public TileTemplate getTile(final Weather weather) {
		return (Requires.notNull(tiles.get(Requires.notNull(weather, "weather == null")), "No tile for weather " + weather + " found."));
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final WeatherTileImpl cast = (WeatherTileImpl) obj;
		return(cast.name.equals(name) && cast.tiles.equals(tiles));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(name, tiles));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[name=" + name + ",tiles=" + tiles + "]");
	}
}
