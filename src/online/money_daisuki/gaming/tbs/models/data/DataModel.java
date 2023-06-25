package online.money_daisuki.gaming.tbs.models.data;

public interface DataModel {
	
	int getConnectionCount();
	
	TileConnection getConnection(int i);
	
	int getUnitTemplateCount();
	
	int getTileTemplateCount();
	
	TileTemplate getTileTemplate(int i);
	
	UnitTemplate getUnitTemplate(int unitId);
	
	WeatherTile getWeatherTile(int id);
	
	int getWeatherTileCount();
	
	Weather getWeather(int i);
	
	int getWeatherCount();
	
	int getDriveCount();
	
	Drive getDrive(int i);
	
}
