package online.money_daisuki.gaming.tbs.models.data;

import java.util.Arrays;
import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class DataModelImpl implements DataModel {
	private final Drive[] drives;
	private final Weather[] weathers;
	private final TileConnection[] tileConnections;
	private final TileTemplate[] tiles;
	private final WeatherTile[] weatherTiles;
	private final UnitTemplate[] units;
	
	public DataModelImpl(final Drive[] drives, final Weather[] weathers,
			final TileConnection[] tileConnections, final TileTemplate[] tiles, final WeatherTile[] weatherTiles,
			final UnitTemplate[] units) {
		this.drives = Requires.containsNotNull(Arrays.copyOf(drives, Requires.notNull(drives, "drives == null").length));
		this.weathers = Requires.containsNotNull(Arrays.copyOf(weathers, Requires.notNull(weathers, "weathers == null").length));
		this.tileConnections = Requires.containsNotNull(Arrays.copyOf(tileConnections, Requires.notNull(tileConnections, "tileConnections == null").length));
		this.tiles = Requires.containsNotNull(Arrays.copyOf(tiles, Requires.notNull(tiles, "tiles == null").length));
		this.weatherTiles = Requires.containsNotNull(Arrays.copyOf(weatherTiles, Requires.notNull(weatherTiles, "weatherTiles == null").length));
		this.units = Requires.containsNotNull(Arrays.copyOf(units, Requires.notNull(units, "units == null").length));
	}
	
	@Override
	public Weather getWeather(final int id) {
		return(weathers[id]);
	}
	@Override
	public int getWeatherCount() {
		return(weathers.length);
	}
	
	@Override
	public int getConnectionCount() {
		return(tileConnections.length);
	}
	@Override
	public TileConnection getConnection(final int i) {
		return(tileConnections[i]);
	}
	
	@Override
	public int getTileTemplateCount() {
		return(tiles.length);
	}
	@Override
	public TileTemplate getTileTemplate(final int i) {
		return(tiles[i]);
	}
	
	@Override
	public WeatherTile getWeatherTile(final int id) {
		return(weatherTiles[id]);
	}
	@Override
	public int getWeatherTileCount() {
		return(weatherTiles.length);
	}
	
	@Override
	public int getUnitTemplateCount() {
		return(units.length);
	}
	@Override
	public UnitTemplate getUnitTemplate(final int id) {
		return(units[id]);
	}
	
	@Override
	public Drive getDrive(final int i) {
		return(drives[i]);
	}
	@Override
	public int getDriveCount() {
		return(drives.length);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final DataModelImpl cast = (DataModelImpl) obj;
		return(Arrays.deepEquals(cast.drives, drives) && Arrays.deepEquals(cast.weathers, weathers) &&
				Arrays.deepEquals(cast.weatherTiles, weatherTiles) && Arrays.deepEquals(cast.weatherTiles, weatherTiles) &&
				Arrays.deepEquals(cast.units, units));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(Arrays.deepHashCode(drives), Arrays.deepHashCode(weathers),
				Arrays.deepHashCode(tileConnections), Arrays.deepHashCode(weatherTiles), Arrays.deepHashCode(units)));
	}
}
