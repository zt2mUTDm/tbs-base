package online.money_daisuki.gaming.tbs.models.game;

public interface TileField {
	
	int getTileType(int id);
	
	int getTileCount();
	
	Integer getConnectionCount(int id);
	
	Integer getConnectedTile(int id, int connection);
	
	boolean isTileConnected(int id, int connection);
	
}
