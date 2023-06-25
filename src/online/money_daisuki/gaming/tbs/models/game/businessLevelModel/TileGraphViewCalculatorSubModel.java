package online.money_daisuki.gaming.tbs.models.game.businessLevelModel;

public interface TileGraphViewCalculatorSubModel {

	public int getTileConnectionCount(Integer tileId);
	
	public Integer getConnectedTile(Integer tileId, int i);
	
	public boolean isTileConnected(Integer tileId, int i);
	
}
